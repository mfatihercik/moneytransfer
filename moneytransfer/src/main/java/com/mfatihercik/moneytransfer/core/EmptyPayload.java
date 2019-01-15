package com.mfatihercik.moneytransfer.core;

public class EmptyPayload implements Validatable {

	@Override
	public boolean validate() {
		return true;
	}

}
