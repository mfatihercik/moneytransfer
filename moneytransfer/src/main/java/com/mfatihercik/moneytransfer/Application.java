package com.mfatihercik.moneytransfer;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mfatihercik.moneytransfer.api.account.AccountController;
import com.mfatihercik.moneytransfer.api.account.AccountDao;
import com.mfatihercik.moneytransfer.api.account.AccountMapper;
import com.mfatihercik.moneytransfer.api.account.AccountOperationMapper;
import com.mfatihercik.moneytransfer.api.account.AccountService;
import com.mfatihercik.moneytransfer.api.user.UserController;
import com.mfatihercik.moneytransfer.api.user.UserDao;
import com.mfatihercik.moneytransfer.api.user.UserMapper;
import com.mfatihercik.moneytransfer.api.user.UserService;
import com.mfatihercik.moneytransfer.core.ApplicationContext;
import com.mfatihercik.moneytransfer.core.DB;
import com.zaxxer.hikari.HikariDataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import spark.Spark;

public class Application {
	private static final String LIQUIBASE_CONFIG_FILE = "liquibase.yaml";
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws SQLException, LiquibaseException, IOException {

		LOG.info("Start initilizing aplication");
		ApplicationContext di = initApplication();
		Spark.init();
		boundApplicationContextToRequestThread(di);

		registerRoutes();
		LOG.debug("Routes initialized");

		Spark.awaitInitialization();
		LOG.info("Server Started");
		Spark.port();
	}

	private static void registerRoutes() {
		registerAccountRoutes();
		registerUserRoutes();
	}

	/**
	 * Register User routes
	 * 
	 * @param userController
	 */
	private static void registerUserRoutes() {
		UserController userController = ApplicationContext.get(UserController.class);
		post(UserController.Path.CREATE, userController.CREATE);
		get(UserController.Path.GET_BY_ID, userController.GET);
		put(UserController.Path.UPDATE, userController.UPDATE);
		get(UserController.Path.LIST_ALL, userController.LIST_ALL);
		get(UserController.Path.ACCOUNTS, userController.ACCOUNTS);
		get(UserController.Path.LIST_ALL, userController.LIST_ALL);
	}

	/**
	 * register Account routes
	 */
	private static void registerAccountRoutes() {
		AccountController accountController = ApplicationContext.get(AccountController.class);

		post(AccountController.Path.CREATE, accountController.CREATE);
		get(AccountController.Path.GET_BY_ID, accountController.GET);
		put(AccountController.Path.UPDATE, accountController.UPDATE);
		get(AccountController.Path.LIST_ALL, accountController.LIST_ALL);
		get(AccountController.Path.OPERATIONS, accountController.OPERATIONS);
		get(AccountController.Path.OPERATION_DETAIL, accountController.OPERATION_DETAIL);
		post(AccountController.Path.TRANSFER_BETWEN_ACCOUNT, accountController.TRANSFER_BETWEN_ACCOUNT);
	}

	/**
	 * start initialize DB and Application Contexxt
	 * 
	 * @return
	 * @throws DatabaseException
	 * @throws SQLException
	 * @throws LiquibaseException
	 */
	public static ApplicationContext initApplication() throws DatabaseException, SQLException, LiquibaseException {
		ApplicationContext di = initApplicationContext();

		HikariDataSource ds = createDataSource();
		loadLiquiBaseConfig(ds);
		LOG.info("Liquibase chage file loaded");
		SqlSessionFactory sqlSessionFactory = configureMybatis(ds);
		ApplicationContext.register(SqlSessionFactory.class, sqlSessionFactory);
		LOG.info("MyBatis Mapper is registered");
		return di;
	}

	/**
	 * Configure mybatis and register Mappers
	 * 
	 * @param ds
	 * @return
	 */
	private static SqlSessionFactory configureMybatis(HikariDataSource ds) {
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, ds);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(AccountMapper.class);
		configuration.addMapper(UserMapper.class);
		configuration.addMapper(AccountOperationMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
		return sqlSessionFactory;
	}

	/**
	 * Create data source.
	 * 
	 * @return
	 */
	private static HikariDataSource createDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;"); // DATABASE_TO_UPPER=false;
		return ds;
	}

	/**
	 * Load database DDL (table creation) and initial DML statement (insert
	 * statement) with LiquiBase.
	 * 
	 * 
	 * @param ds
	 * @throws DatabaseException
	 * @throws SQLException
	 * @throws LiquibaseException
	 */
	private static void loadLiquiBaseConfig(HikariDataSource ds) throws DatabaseException, SQLException, LiquibaseException {
		String liquibasePath = LIQUIBASE_CONFIG_FILE;

		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(ds.getConnection()));
		Liquibase liquibase = new Liquibase(liquibasePath, new ClassLoaderResourceAccessor(Application.class.getClassLoader()), database);

		liquibase.update("main");
	}

	/**
	 * Bound request and database transaction to current thread. and remove after
	 * execution is finished
	 * 
	 * @param diContainer
	 */
	private static void boundApplicationContextToRequestThread(ApplicationContext diContainer) {
		before((req, res) -> {
			ApplicationContext.setInstance(diContainer);
			DB.openSession();
		});
		after((req, res) -> {
			ApplicationContext.removetInstance();
			DB.removeSession();
		});
	}

	/**
	 * initialize IoC container
	 * 
	 * @return
	 */
	private static ApplicationContext initApplicationContext() {
		ApplicationContext.register(new AccountController());
		ApplicationContext.register(new AccountDao());
		ApplicationContext.register(new AccountService());
		ApplicationContext.register(new UserController());
		ApplicationContext.register(new UserDao());
		ApplicationContext.register(new UserService());
		return ApplicationContext.getInstance();
	}

}
