package com.mfatihercik.moneytransfer.domain;

import java.math.BigDecimal;

import com.mfatihercik.moneytransfer.core.AuditModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class AccountOperation extends AuditModel {
	
	public AccountOperation() {
		
	}

	public AccountOperation(Account account, BigDecimal amount, String transactionId, Type type, String description) {
		this.account = account;
		this.type = type;
		this.amount = amount.abs();
		this.transactionId = transactionId;
		this.description = description;
		if (Type.CREDIT == type) {
			this.amount = amount.negate();
		}
	}
	public AccountOperation createMirror(Account account) {
		return new AccountOperation(account, amount, transactionId, type==Type.DEBIT?Type.CREDIT:Type.CREDIT, description);
	}

	public static enum Type {
		DEBIT, CREDIT
	}

	private int id;
	private String transactionId;
	private Account account;
	private BigDecimal amount;
	private Type type;
	private String description;

}
