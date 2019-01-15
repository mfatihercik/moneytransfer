package com.mfatihercik.moneytransfer.api.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mfatihercik.moneytransfer.domain.User;

public interface UserMapper {
	void insert(User account);

	List<User> findAll();

	User get(@Param("id") int id);

	void update(User account);

}
