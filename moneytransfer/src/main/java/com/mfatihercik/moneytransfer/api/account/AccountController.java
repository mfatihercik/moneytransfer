package com.mfatihercik.moneytransfer.api.account;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mfatihercik.moneytransfer.core.Answer;
import com.mfatihercik.moneytransfer.core.ApplicationContext;
import com.mfatihercik.moneytransfer.core.EmptyPayload;
import com.mfatihercik.moneytransfer.core.RequestHandler;
import com.mfatihercik.moneytransfer.core.ValidationException;
import com.mfatihercik.moneytransfer.domain.Account;

import spark.Route;

/**
 * this class has Account specific methods. 
 * this class only read parameter from params map and delegate request to AccountService
 * 
 * @author fatih.ercik
 *
 */
public class AccountController {

	/**
	 * create operation for account.
	 * 
	 */
	public final Route CREATE = new RequestHandler<Account>() {
		@Override
		public Object process(Account value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			AccountService accountService = ApplicationContext.get(AccountService.class);
			return accountService.create(value);
		}
	};
	/**
	 * 
	 */
	public final Route UPDATE = new RequestHandler<Account>() {
		@Override
		public Object process(Account value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			AccountService accountService = ApplicationContext.get(AccountService.class);
			return accountService.update(value);
		}
	};

	/**
	 * get account with given id
	 * 
	 * @param id
	 */
	public final Route GET = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			AccountService accountService = ApplicationContext.get(AccountService.class);
			String id = validate(params);
			Account response = accountService.get(Integer.valueOf(id));
			if (response == null)
				return new Answer(404);
			return response;
		}

		private String validate(Map<String, String> params) {
			String id = params.get(":id");

			if (id == null)
				throw new ValidationException(400);
			return id;
		}
	};

	/**
	 * List all account. Maybe filtered by status.
	 * 
	 * @param status
	 *            one or multiple Account.STATUS that seperated with comma
	 */
	public final Route LIST_ALL = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			AccountService accountService = ApplicationContext.get(AccountService.class);
			String status = queryParamsMap.containsKey("status") ? queryParamsMap.get("status")[0] : "";
			List<String> list = Arrays.asList();
			if (!status.isEmpty())
				list = Arrays.asList(status.split(","));
			return accountService.findAll(list);
		}
	};
	/**
	 * list all account operation of an account
	 */
	public final Route OPERATIONS = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {

			AccountService accountService = ApplicationContext.get(AccountService.class);
			String id = validate(params);
			Account response = accountService.get(Integer.valueOf(id));
			if (response == null)
				return null;
			return accountService.getAccountOperations(response.getAccountId());

		}

		private String validate(Map<String, String> params) {
			String id = params.get(":id");
			if (id == null)
				throw new ValidationException(400);
			return id;
		}
	};
	/**
	 * list all account operation of an account
	 */
	public final Route OPERATION_DETAIL = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {

			AccountService accountService = ApplicationContext.get(AccountService.class);
			String id = validate(params);

			return accountService.getOperation(Integer.valueOf(id));

		}

		private String validate(Map<String, String> params) {
			String id = params.get(":id");
			if (id == null)
				throw new ValidationException(400);
			return id;
		}
	};
	public final Route TRANSFER_BETWEN_ACCOUNT = new RequestHandler<AccountTransferDto>() {
		@Override
		public Object process(AccountTransferDto value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			AccountService accountService = ApplicationContext.get(AccountService.class);
			return accountService.transferBetwenAccount(value);

		}
	};

	public static class Path {
		public static final String CREATE = "/account";
		public static final String LIST_ALL = "/account";
		public static final String GET_BY_ID = "/account/:id";
		public static final String UPDATE = "/account/:id";
		public static final String OPERATIONS = "/account/:id/operations";
		public static final String OPERATION_DETAIL = "/operation/:id";
		public static final String TRANSFER_BETWEN_ACCOUNT = "/account/transfer";
	}

}
