package com.mfatihercik.moneytransfer.api.account;

import java.math.BigDecimal;

import com.mfatihercik.moneytransfer.core.Validatable;

import lombok.Data;

/**
 * Dto for AccountController.transfer end point.
 * @author fatih.ercik
 *
 */
@Data
public class AccountTransferDto implements Validatable {
	private Integer sourceAccountId;
	private Integer destinationAccountId;
	private BigDecimal amount;
	private String description;

	@Override
	public boolean validate() {
		return sourceAccountId != null && destinationAccountId != null && amount != null;
	}

}
