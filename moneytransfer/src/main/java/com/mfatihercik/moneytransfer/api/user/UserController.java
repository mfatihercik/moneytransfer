package com.mfatihercik.moneytransfer.api.user;

import java.util.Map;

import com.mfatihercik.moneytransfer.core.Answer;
import com.mfatihercik.moneytransfer.core.ApplicationContext;
import com.mfatihercik.moneytransfer.core.EmptyPayload;
import com.mfatihercik.moneytransfer.core.RequestHandler;
import com.mfatihercik.moneytransfer.domain.User;

import spark.Route;

/**
 * this class contains User specific methods. 
 * this class only read parameter from params map and delegate request to UserService
 * 
 * @author fatih.ercik
 *
 */
public class UserController {

	/**
	 * create operation for user.
	 * 
	 */
	public final Route CREATE = new RequestHandler<User>() {
		@Override
		public Object process(User value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			UserService userService = ApplicationContext.get(UserService.class);
			return userService.create(value);
		}
	};
	/**
	 * 
	 */
	public final Route UPDATE = new RequestHandler<User>() {
		@Override
		public Object process(User value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			UserService userService = ApplicationContext.get(UserService.class);
			String id = params.get(":id");
			if (id == null)
				return new Answer(400);
			value.setId(Integer.valueOf(id));
			return userService.update(value);
		}
	};

	/**
	 * get user with given id
	 * 
	 * @param id
	 */
	public final Route GET = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			UserService userService = ApplicationContext.get(UserService.class);
			String id = params.get(":id");

			if (id == null)
				return new Answer(400);
			User response = userService.get(Integer.valueOf(id));
			if (response == null)
				return new Answer(404);
			return response;
		}
	};

	/**
	 * List all user
	 */
	public final Route LIST_ALL = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			UserService userService = ApplicationContext.get(UserService.class);
			return userService.findAll();

		}
	};
	/**
	 * list user accounts
	 */
	public final Route ACCOUNTS = new RequestHandler<EmptyPayload>() {
		@Override
		public Object process(EmptyPayload value, Map<String, String> params, Map<String, String[]> queryParamsMap) {
			UserService userService = ApplicationContext.get(UserService.class);
			String id = params.get(":id");

			if (id == null)
				return new Answer(400);
			return userService.getAccounts(Integer.valueOf(id));

		}
	};

	public static class Path {
		public static final String CREATE = "/user";
		public static final String LIST_ALL = "/user";
		public static final String GET_BY_ID = "/user/:id";
		public static final String UPDATE = "/user/:id";
		public static final String ACCOUNTS = "/user/:id/account";
	}

}
