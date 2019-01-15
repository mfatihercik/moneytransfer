package com.mfatihercik.moneytransfer.api.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mfatihercik.moneytransfer.domain.AccountOperation;

public interface AccountOperationMapper {
	void insert(AccountOperation account);

	AccountOperation get(@Param("id") int id);

	List<AccountOperation> getAccountOperations(int accountId);

	AccountOperation get(String id);

	
}
