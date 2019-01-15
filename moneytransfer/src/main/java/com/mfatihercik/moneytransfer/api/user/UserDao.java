package com.mfatihercik.moneytransfer.api.user;

import java.util.List;

import com.mfatihercik.moneytransfer.core.BaseDao;
import com.mfatihercik.moneytransfer.core.DB;
import com.mfatihercik.moneytransfer.domain.User;

/**
 * {@inheritDoc}
 * Data access layer of Account api
 * 
 * Method shouldn't commit transaction. transaction is managed by controller layer or service layer 
 * @author fatih.ercik
 *
 */
public class UserDao extends BaseDao {
	/**
	 * Create new user with given parammeter. return user id.
	 * 
	 * @param User
	 *            user
	 * @return User
	 */
	public int create(User value) {
		UserMapper mapper = DB.getMapper(UserMapper.class);
		mapper.insert(value);
		return value.getId();

	}

	/**
	 * get user by given id
	 * 
	 * @param id
	 * @return
	 */
	public User get(int id) {
		UserMapper mapper = DB.getMapper(UserMapper.class);
		return mapper.get(id);

	}

	/**
	 * find all user
	 * 
	 * @return list of user
	 */
	public List<User> findAll() {
		UserMapper mapper = DB.getMapper(UserMapper.class);
		return mapper.findAll();
	}

	/**
	 * update user by id
	 * 
	 * @param user
	 * @return
	 */
	public void update(User user) {
		UserMapper mapper = DB.getMapper(UserMapper.class);
		mapper.update(user);
	}
}
