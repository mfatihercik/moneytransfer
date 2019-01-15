package com.mfatihercik.moneytransfer

import com.mfatihercik.moneytransfer.api.account.AccountMapper
import com.mfatihercik.moneytransfer.api.user.UserController
import com.mfatihercik.moneytransfer.api.user.UserDao
import com.mfatihercik.moneytransfer.core.Answer
import com.mfatihercik.moneytransfer.core.ApplicationContext
import com.mfatihercik.moneytransfer.core.DB
import com.mfatihercik.moneytransfer.core.RequestHandler
import com.mfatihercik.moneytransfer.domain.Account
import com.mfatihercik.moneytransfer.domain.User

import spock.lang.Shared
import spock.lang.Specification

class UserSpec extends Specification {

	@Shared ApplicationContext context=Application.initApplication()

	@Shared UserController userController=context.get(UserController.class)

	@Shared UserDao userDao=Stub()

	def setup() {
		DB.openSession()
	}



	def "CREATE: should create"(){

		RequestHandler create= userController.CREATE

		def user=new User(name: "test");

		UserDao userDao =Stub()
		ApplicationContext.register(UserDao.class,userDao)

		when:
		userDao.create(_) >> 123
		userDao.get(_) >>{user.id=123;return user}
		def a= create.process(user, [:],[:])


		then:
		a.id
	}
	def "GET : #id parameter is required(bad request)"(){

		RequestHandler get= userController.GET
		UserDao userDao =Stub()
		ApplicationContext.register(UserDao.class,userDao)
		when:

		def a= get.process(null, [:],[:])

		then:
		a instanceof  Answer
		a.code==400
	}
	def "GET : should not found"(){

		RequestHandler get= userController.GET
		UserDao userDao =Stub()
		ApplicationContext.register(UserDao.class,userDao)
		when:
		userDao.get(_)>>null
		def a= get.process(null, [":id":"123413412"],[:])

		then:
		a instanceof  Answer
		a.code==404
	}
	def "GET : should return user"(){



		RequestHandler get= userController.GET
		UserDao userDao =Stub()

		ApplicationContext.register(UserDao.class,userDao)

		when:

		userDao.get(_) >> new User(id: 40)
		def a= get.process(null, [":id":"1"],[:])

		then:

		a instanceof  User
		a.id==40
	}
	def "LIST_ALL :  should return user list"(){

		RequestHandler LIST_ALL= userController.LIST_ALL
		UserDao userDao =Stub()
		ApplicationContext.register(UserDao.class,userDao)

		when:
		userDao.findAll() >> [new User(id: 1), new User(id: 2)]
		def a= LIST_ALL.process(null, [:],[:])

		then:
		a instanceof  List
		a.size()==2
	}
	def "ACCOUNTS :  list all account for user"(){

		RequestHandler ACCOUNTS= userController.ACCOUNTS
		AccountMapper accountDao =Stub()
		ApplicationContext.register(AccountMapper.class,accountDao)

		when:
		accountDao.getUserAccount(_) >> [new Account(accountId: 1), new Account(accountId: 2)]
		def a= ACCOUNTS.process(null, [":id":"1"],[:])

		then:
		a instanceof  List
		a.size()==2
	}
	def "UPDATE :  should update record"(){

		RequestHandler UPDATE= userController.UPDATE
		UserDao userDao =Stub()
		ApplicationContext.register(UserDao.class,userDao)

		def user=new User(name: "test",id: 123);

		when:

		userDao.update(_)>>null
		userDao.get(_) >> user
		def a= UPDATE.process(user, [":id":"123"],[:])

		then:
		a
		a.id==123
	}
}
