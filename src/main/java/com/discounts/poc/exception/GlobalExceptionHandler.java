package com.discounts.poc.exception;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.discounts.poc.Constants;
import com.discounts.poc.model.ResponseObject;

@ControllerAdvice
public class GlobalExceptionHandler {


	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ResponseObject handleResourceNotFoundException(final ResourceNotFoundException exception,
			final HttpServletRequest request) {

		logger.error(HttpStatus.NOT_FOUND + " : " + exception.getMessage());

		ResponseObject obj = new ResponseObject();
		obj.setMessage(exception.getMessage());
		obj.setStatus(Constants.failure);
		obj.setHttpStatus(HttpStatus.NOT_FOUND);
		obj.setHttpStatusCode(HttpStatus.NOT_FOUND.value());

		return obj;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ResponseObject handleException(final Exception exception, final HttpServletRequest request) {

		logger.error("Exception occured!", exception);
		logger.error(HttpStatus.INTERNAL_SERVER_ERROR + " : " + exception.getMessage());
		ResponseObject obj = new ResponseObject();
		obj.setMessage(exception.getMessage());
		obj.setStatus(Constants.failure);
		obj.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		obj.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		return obj;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ResponseObject handleConstraintViolationException(final ConstraintViolationException exception,
			final HttpServletRequest request) {

		logger.error(HttpStatus.BAD_REQUEST + " : " + exception.getMessage());

		ResponseObject obj = new ResponseObject();
		obj.setMessage(exception.getMessage());
		obj.setStatus(Constants.failure);
		obj.setHttpStatus(HttpStatus.BAD_REQUEST);
		obj.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());

		return obj;
	}

}
