package com.mfatihercik.moneytransfer

import com.mfatihercik.moneytransfer.Application
import com.mfatihercik.moneytransfer.api.account.AccountController
import com.mfatihercik.moneytransfer.api.account.AccountDao
import com.mfatihercik.moneytransfer.api.account.AccountService
import com.mfatihercik.moneytransfer.api.account.AccountTransferDto
import com.mfatihercik.moneytransfer.core.Answer
import com.mfatihercik.moneytransfer.core.ApplicationContext
import com.mfatihercik.moneytransfer.core.DB
import com.mfatihercik.moneytransfer.core.RequestHandler
import com.mfatihercik.moneytransfer.core.ValidationException
import com.mfatihercik.moneytransfer.domain.Account
import com.mfatihercik.moneytransfer.domain.User
import com.mfatihercik.moneytransfer.domain.Account.STATUS
import com.mfatihercik.moneytransfer.domain.AccountOperation

import spock.lang.Shared
import spock.lang.Specification

class AccountSpec extends Specification {

	@Shared ApplicationContext context=Application.initApplication()

	@Shared AccountController accountController=context.get(AccountController.class)

	@Shared AccountDao accountDao=Stub()

	def setup() {
		DB.openSession()
	}



	def "CREATE: should create"(){

		RequestHandler create= accountController.CREATE

		def account=new Account(status: STATUS.ACTIVE,currency: "USD",owner: new User(id:4));

		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)

		when:
		accountDao.create(_) >> 123
		accountDao.get(_) >>{account.accountId=123;account.owner.name="fatih"; account}
		def a= create.process(account, [:],[:])


		then:
		a.owner.name
		a.accountId
	}
	def "GET : #id parameter is required(bad request)"(){

		RequestHandler get= accountController.GET
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)
		when:

		def a= get.process(null, [:],[:])

		then:
		def e=thrown(ValidationException.class)
		e.code==400
	}
	def "GET : should not found"(){

		RequestHandler get= accountController.GET
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)
		when:
		accountDao.get(_)>>null
		def a= get.process(null, [":id":"123413412"],[:])

		then:
		a instanceof  Answer
		a.code==404
	}
	def "GET : should return account"(){



		RequestHandler get= accountController.GET
		AccountDao accountDao =Stub()

		ApplicationContext.register(AccountDao.class,accountDao)

		when:

		accountDao.get(_) >> new Account(accountId: 40)
		def a= get.process(null, [":id":"1"],[:])

		then:

		print(a)
		a instanceof  Account
		a.accountId==40
	}
	def "LIST_ALL :  should return account list"(){

		RequestHandler LIST_ALL= accountController.LIST_ALL
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)

		when:
		accountDao.findAll(_) >> [new Account(accountId: 1), new Account(accountId: 2)]
		def a= LIST_ALL.process(null, [:],[:])

		then:
		a instanceof  List
		a.size()==2
	}
	def "UPDATE :  should update record"(){

		RequestHandler UPDATE= accountController.UPDATE
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)

		def account=new Account(accountId: 123,status: STATUS.ACTIVE,currency: "USD",owner: new User(id:4));


		when:

		accountDao.update(_)>> account

		def a= UPDATE.process(null, [:],[:])

		then:
		a
		a.accountId==123
	}
	def "OPERATIONS :  #id paramter is required"(){
		RequestHandler OPERATIONS= accountController.OPERATIONS
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)

		when:
		def a= OPERATIONS.process(null, [:],[:])

		then:
		def e=thrown(ValidationException)
		e.code==400
	}
	def "OPERATIONS :  should list operation belogs to given operation"(){

		RequestHandler OPERATIONS= accountController.OPERATIONS
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)

		when:
		accountDao.getAccountOperations(_) >> [new AccountOperation(id: 1), new AccountOperation(id: 2)]
		def a= OPERATIONS.process(null, [":id":"2"],[:])

		then:
		a instanceof  List
		a.size()==2
	}
	def "OPERATION_DETAIL :  should get operation detail"(){

		RequestHandler handler= accountController.OPERATION_DETAIL
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)

		when:
		accountDao.getOperation(_) >> new AccountOperation(id: 1)
		def a= handler.process(null, [":id":"2"],[:])

		then:
		a instanceof  AccountOperation
		a.id==1
	}
	def "TRANSFER_BETWEN_ACCOUNT :  should transfer between account"(){

		RequestHandler handler= accountController.TRANSFER_BETWEN_ACCOUNT
		AccountDao accountDao =Stub()
		ApplicationContext.register(AccountDao.class,accountDao)
		def transfer=new AccountTransferDto(sourceAccountId: 1,destinationAccountId: 2,amount: 10);
		Account sourceAccount=new Account(accountId: 1,balance: 100)
		Account  destinationAccount =new Account(accountId: 2,balance: 100)

		when:
		accountDao.isAccountExist(_) >> true
		accountDao.get(1) >>sourceAccount
		accountDao.get(2) >>destinationAccount
		accountDao.update(sourceAccount) >>{ sourceAccount.balance=90;sourceAccount }
		accountDao.update(destinationAccount) >>{ destinationAccount.balance=110;destinationAccount }
		accountDao.createOperation(_) >> 2
		accountDao.getOperation(_) >> new AccountOperation(id:2)
		def a= handler.process(transfer, [:],[:])

		then:
		a instanceof  List
		a.size()==2
	}
}
