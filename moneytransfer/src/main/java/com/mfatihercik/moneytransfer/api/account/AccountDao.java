package com.mfatihercik.moneytransfer.api.account;

import java.util.List;

import com.mfatihercik.moneytransfer.core.BaseDao;
import com.mfatihercik.moneytransfer.core.DB;
import com.mfatihercik.moneytransfer.domain.Account;
import com.mfatihercik.moneytransfer.domain.AccountOperation;
/**
 * {@inheritDoc}
 * 
 * Data access layer of Account api
 * 
 * Method shouldn't commit transaction. transaction is managed by controller layer or service layer 
 * @author fatih.ercik
 *
 */
public class AccountDao extends BaseDao {

	/**
	 * Create new account with given parammeter. return account id.
	 * 
	 * @param Account
	 *            account
	 * @return Account
	 */
	public int create(Account value) {
		AccountMapper mapper = DB.getMapper(AccountMapper.class);
		mapper.insert(value);
		return value.getAccountId();

	}

	/**
	 * get account by given id
	 * 
	 * @param id
	 * @return
	 */
	public Account get(int id) {
		AccountMapper mapper = DB.getMapper(AccountMapper.class);
		return mapper.get(id);

	}

	/**
	 * 
	 * Find all account. can be filtered by status field with multiple
	 * Account.STATUS list
	 * 
	 * @param list
	 *            list of Account.STATUS
	 * @return
	 */
	public List<Account> findAll(List<String> statuses) {
		AccountMapper mapper = DB.getMapper(AccountMapper.class);
		return mapper.findAll(statuses);
	}

	/**
	 * update account by id
	 * 
	 * @param account
	 * @return
	 */
	public Account update(Account account) {
		AccountMapper mapper = DB.getMapper(AccountMapper.class);
		mapper.update(account);
		return get(account.getAccountId());
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<Account> getUserAccount(Integer userId) {
		AccountMapper mapper = DB.getMapper(AccountMapper.class);
		return mapper.getUserAccount(userId);
	}

	public List<AccountOperation> getAccountOperations(int accountId) {
		AccountOperationMapper mapper = DB.getMapper(AccountOperationMapper.class);
		return mapper.getAccountOperations(accountId);
	}

	public AccountOperation getOperation(int id) {
		AccountOperationMapper mapper = DB.getMapper(AccountOperationMapper.class);
		return mapper.get(id);
	}

	public boolean isAccountExist(int accountId) {
		AccountMapper mapper = DB.getMapper(AccountMapper.class);
		return mapper.isAccountExist(accountId)>0; 
		
	}

	public int createOperation(AccountOperation value) {
		AccountOperationMapper mapper = DB.getMapper(AccountOperationMapper.class);
		mapper.insert(value);
		return value.getId();
	}

}
