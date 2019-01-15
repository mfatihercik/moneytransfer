package com.mfatihercik.moneytransfer.domain;

import java.math.BigDecimal;

import com.mfatihercik.moneytransfer.core.AuditModel;
import com.mfatihercik.moneytransfer.core.Validatable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends AuditModel implements Validatable {

	public static enum STATUS {
		ACTIVE, PENDING, INACTIVE
	}

	private int accountId;
	private BigDecimal balance;
	private User owner;
	private String currency;
	private STATUS status;

	@Override
	public boolean validate() {
		return this.currency != null && this.status != null && owner != null;
	}

}
