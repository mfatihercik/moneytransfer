package com.mfatihercik.moneytransfer.api.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mfatihercik.moneytransfer.domain.Account;

public interface AccountMapper {

	void insert(Account account);

	List<Account> findAll(List<String> list);

	Account get(@Param("id") int id);

	void update(Account account);

	List<Account> getUserAccount(Integer ownerId);
	
	int isAccountExist(int id);

	

}
