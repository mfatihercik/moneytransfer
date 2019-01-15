package com.mfatihercik.moneytransfer.api.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.mfatihercik.moneytransfer.core.ApplicationContext;
import com.mfatihercik.moneytransfer.core.BaseService;
import com.mfatihercik.moneytransfer.core.DB;
import com.mfatihercik.moneytransfer.core.ValidationException;
import com.mfatihercik.moneytransfer.domain.Account;
import com.mfatihercik.moneytransfer.domain.AccountOperation;
import com.mfatihercik.moneytransfer.domain.AccountOperation.Type;


/**
 * {@inheritDoc}
 * Service layer for Account api.
 * 
 * Service layer may have transactional methods.  
 * 
 * @author fatih.ercik
 *
 */
public class AccountService extends BaseService {

	/**
	 * create new account with given parameter and return created account
	 * 
	 * @param Account
	 *            value
	 * @return Account
	 */
	public Account create(Account value) {
		AccountDao accountDao = getAccountDao();
		int id = accountDao.create(value);
		Account account = accountDao.get(id);
		DB.commit();
		return account;
	}

	/**
	 * create new account operation with given parameter and return created account
	 * 
	 * @param Account
	 *            value
	 * @return Account
	 */
	public AccountOperation create(AccountOperation value) {
		AccountDao accountDao = getAccountDao();
		int id = accountDao.createOperation(value);
		return getOperation(id);

	}

	/**
	 * Find all accounts
	 * 
	 * @param list
	 *            status list
	 * @param value
	 * @return
	 */
	public List<Account> findAll(List<String> list) {
		AccountDao accountDao = getAccountDao();
		return accountDao.findAll(list);

	}

	/**
	 * get account by id
	 * 
	 * @param id
	 *            account id of Account
	 * @return
	 */
	public Account get(int id) {
		return getAccountDao().get(id);
	}

	/**
	 * check if is account exist
	 * 
	 * @param id
	 *            account id of Account
	 * @return
	 */
	public boolean isAccountExist(int id) {
		return getAccountDao().isAccountExist(id);
	}

	/**
	 * get AccountDao from DI container.
	 * 
	 * @return AccountDao
	 */
	public AccountDao getAccountDao() {
		return ApplicationContext.get(AccountDao.class);
	}

	/**
	 * update account
	 * 
	 * @param account
	 * @return
	 */
	public Account update(Account account) {
		return getAccountDao().update(account);

	}

	/**
	 * get all operation belongs to an account
	 * 
	 * @param accountId
	 *            accountId of account
	 * @return
	 */
	public List<AccountOperation> getAccountOperations(int accountId) {
		return getAccountDao().getAccountOperations(accountId);
	}

	/**
	 * get operation with detail
	 * 
	 * @param id
	 * @return
	 */
	public AccountOperation getOperation(int id) {
		return getAccountDao().getOperation(id);

	}

	/**
	 * transfer amount from source account to destination account. balance of source
	 * and destination account is updated.
	 * 
	 * 
	 * @param trasnferDto
	 * @return list of AccountOperation
	 */
	public List<AccountOperation> transferBetwenAccount(AccountTransferDto trasnferDto) {
		validate(trasnferDto);

		Account sourceAccount = get(trasnferDto.getSourceAccountId());
		Account destinationAccount = get(trasnferDto.getDestinationAccountId());
		BigDecimal amount = trasnferDto.getAmount().abs();

		setNewBalanceOfAccount(amount.negate(), sourceAccount);
		setNewBalanceOfAccount(amount, destinationAccount);

		String transactionId = UUID.randomUUID().toString();
		AccountOperation debit = new AccountOperation(sourceAccount, amount, transactionId, Type.DEBIT, trasnferDto.getDescription());
		AccountOperation credit = debit.createMirror(destinationAccount);
		update(sourceAccount);
		update(destinationAccount);
		create(credit);
		create(debit);
		DB.commit();
		return Arrays.asList(credit, debit);

	}

	private void setNewBalanceOfAccount(BigDecimal amount, Account account) {
		account.setBalance(account.getBalance().add(amount).setScale(2, RoundingMode.HALF_UP));
	}

	private void validate(AccountTransferDto trasnferDto) {
		if (!isAccountExist(trasnferDto.getSourceAccountId()))
			throw new ValidationException(400, "Source account is not defined");
		if (!isAccountExist(trasnferDto.getDestinationAccountId()))
			throw new ValidationException(400, "Destination account is not defined");
	}

}
