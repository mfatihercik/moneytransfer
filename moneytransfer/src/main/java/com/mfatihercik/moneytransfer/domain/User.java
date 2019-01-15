package com.mfatihercik.moneytransfer.domain;

import com.mfatihercik.moneytransfer.core.AuditModel;
import com.mfatihercik.moneytransfer.core.Validatable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AuditModel implements Validatable {
	private int id;
	private String name;
	@Override
	public boolean validate() {
		return name!=null;
	}
}
