package com.mfatihercik.moneytransfer.core;

import java.util.Date;

import com.mfatihercik.moneytransfer.domain.User;

import lombok.Getter;
import lombok.Setter;
/**
 * Base domain object for all domain.
 * @author fatih.ercik
 *
 */
@Getter
@Setter
public abstract class AuditModel {	
	private Date createdDate;
	private Date modifiedDate;
	private User createdBy;
	private User modifiedBy;	

}
