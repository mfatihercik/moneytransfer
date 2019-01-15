package com.mfatihercik.moneytransfer.core;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * it holds ThreadLocal {@link SqlSession} object that initialize on ever
 * request. On every request, new transaction is started and closed after
 * request finished
 * 
 * @author fatih.ercik
 *
 */
public class DB {

	public static ThreadLocal<SqlSession> _thread = new ThreadLocal<>();

	private DB() {

	}

	public static <T> T getMapper(Class<T> clazz) {
		if (_thread.get() == null)
			openSession();
		return (T) _thread.get().getMapper(clazz);
	}

	public static void openSession() {
		_thread.remove();
		setSession(ApplicationContext.get(SqlSessionFactory.class).openSession());
	}

	public static void setSession(SqlSession instance) {
		_thread.set(instance);
	}

	public static void removeSession() {
		_thread.remove();
	}

	/**
	 * commit current transaction and start new transaction
	 */
	public static void commit() {
		_thread.get().commit();
		openSession();
	}

	/**
	 * rolback current transaction and start new transaction
	 */
	public static void rolback() {
		_thread.get().rollback();
		openSession();
	}

}
