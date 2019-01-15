package com.mfatihercik.moneytransfer.core;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import spark.Request;
import spark.Response;
import spark.Route;
/**
 * Base class for every request.
 * 
 * it make abstraction between http and service layer.
 * 
 * http payload is automatically deserialized to <V>
 * 
 * and response is serialized to json.
 * 
 * Database transactions are managed by this class.
 * 
 * @author fatih.ercik
 *
 * @param <V>
 */
public abstract class RequestHandler<V extends Validatable> implements Route {

	private static final int HTTP_BAD_REQUEST = 400;

	public RequestHandler() {
		this.valueClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	Class<V> valueClass;
	protected Request request;
	protected Response response;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Object handle(Request request, Response response) throws Exception {
		objectMapper.setSerializationInclusion(Include.NON_NULL);		
		response.type("application/json");
		V value = null;
		if (valueClass != EmptyPayload.class) {
			value = objectMapper.readValue(request.body(), valueClass);
			if (!value.validate()) {
				this.response.status(400);
				return "";
			}
		}

		this.request = request;
		this.response = response;
		try {
			Object responsePayload = process(value, request.params(), request.queryMap().toMap());
			if (responsePayload == null) {
				response.status(404);
				DB.rolback();
				return "";
			}
			Object body = null;
			if (responsePayload instanceof Answer) {
				Answer answer = (Answer) responsePayload;
				body = answer.getBody();
				response.status(answer.getCode());
			} else {
				body = responsePayload;
			}
			String data = null;
			if (body instanceof String) {
				data = body.toString();
			} else {
				data = dataToJson(body);
			}
			response.body(data);
			DB.commit();
			return data;
		} catch (ValidationException e) {
			response.status(e.getCode());
			response.body(e.getMessage());
			response.type("text/html");
			DB.rolback();
			return response.body();

		}
	}

	abstract public Object process(V value, Map<String, String> params, Map<String, String[]> map);

	public  String dataToJson(Object data) {
		try {			
			return objectMapper.writeValueAsString(data);
		} catch (IOException e) {
			throw new RuntimeException("IOException from a StringWriter?");
		}
	}

}
