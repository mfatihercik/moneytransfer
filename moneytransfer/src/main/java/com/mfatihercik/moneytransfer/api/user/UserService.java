package com.mfatihercik.moneytransfer.api.user;

import java.util.List;

import com.mfatihercik.moneytransfer.api.account.AccountDao;
import com.mfatihercik.moneytransfer.core.ApplicationContext;
import com.mfatihercik.moneytransfer.core.DB;
import com.mfatihercik.moneytransfer.domain.Account;
import com.mfatihercik.moneytransfer.domain.User;

/**
 * {@inheritDoc}
 * Service layer for Account api.
 * 
 * Service layer may have transactional methods.
 * 
 * @author fatih.ercik
 *
 */
public class UserService {
	/**
	 * create new user with given parameter and return created user
	 * 
	 * @param User
	 *            value
	 * @return User
	 */
	public User create(User value) {
		UserDao userDao = getUserDao();
		int id = userDao.create(value);
		User user = userDao.get(id);
		DB.commit();
		return user;
	}

	/**
	 * find all user
	 * 
	 * @return list of user
	 */
	public List<User> findAll() {
		UserDao userDao = getUserDao();
		return userDao.findAll();
	}

	/**
	 * get user by id
	 * 
	 * @param id
	 *            user id of User
	 * @return
	 */
	public User get(int id) {
		return getUserDao().get(id);
	}

	/**
	 * get UserDao from DI container.
	 * 
	 * @return UserDao
	 */
	public UserDao getUserDao() {
		return ApplicationContext.get(UserDao.class);
	}

	/**
	 * update user
	 * 
	 * @param user
	 * @return
	 */
	public User update(User user) {
		UserDao userDao = getUserDao();
		userDao.update(user);
		return userDao.get(user.getId());

	}

	/**
	 * get all accounts of user
	 * 
	 * @param userId
	 *            id of user
	 * @return
	 */
	public List<Account> getAccounts(Integer userId) {
		return ApplicationContext.get(AccountDao.class).getUserAccount(userId);
	}
}
