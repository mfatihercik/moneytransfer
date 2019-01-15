
package com.mfatihercik.moneytransfer.core;

import lombok.Getter;
import lombok.Setter;

/**
 * it is used in {@link RequestHandler.process} method to return html specific code and object to set to body.
 *  
 *  
 * 
 * @author fatih.ercik
 *
 */
@Getter
@Setter
public class Answer {
	private int code;
	private Object body;

	public Answer(int code) {
		this.code = code;
		this.body = "";
	}

	public Answer(int code, String body) {
		this.code = code;
		this.body = body;
	}

	public static Answer ok(String body) {
		return new Answer(200, body);
	}

}
