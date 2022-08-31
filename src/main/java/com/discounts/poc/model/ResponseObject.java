package com.discounts.poc.model;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

public class ResponseObject {
	
	private String status;
	private HttpStatus httpStatus;
	private String message;
	private int httpStatusCode;
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
	
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}
	
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
