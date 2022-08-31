package com.discounts.poc.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.discounts.poc.Constants;
import com.discounts.poc.exception.ResourceNotFoundException;
import com.discounts.poc.model.DiscountDetail;
import com.discounts.poc.model.ItemDetail;
import com.discounts.poc.model.ResponseObject;
import com.discounts.poc.service.ManageDiscountsService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping(value = "/api/discounts/v1/")
public class ManageDiscountsController {

	@Autowired
	ManageDiscountsService service;

	@PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseObject addDiscounts(@RequestBody List<DiscountDetail> discounts)
			throws ConstraintViolationException, Exception {
		ResponseObject respObj = new ResponseObject();
		service.addDiscount(discounts);
		respObj.setStatus(Constants.success);
		respObj.setHttpStatus(HttpStatus.OK);
		respObj.setHttpStatusCode(HttpStatus.OK.value());
		return respObj;

	}

	@DeleteMapping(path = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseObject deleteDiscountByCode(@PathVariable(value = "code") String code)
			throws ResourceNotFoundException, Exception {
		ResponseObject respObj = new ResponseObject();
		service.deleteDiscount(code);
		respObj.setStatus(Constants.success);
		respObj.setHttpStatus(HttpStatus.OK);
		respObj.setHttpStatusCode(HttpStatus.OK.value());
		return respObj;

	}

	@GetMapping(path = "/get-best-applicable-discount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseObject getBestApplicableDiscount(HttpServletRequest request)
			throws ConstraintViolationException, Exception {

		ResponseObject respObj = new ResponseObject();

		String param = request.getHeader("items");
		List<ItemDetail> items;

		try {
			items = new Gson().fromJson(param, new TypeToken<ArrayList<ItemDetail>>() {
			}.getType());
		} catch (Exception e) {
			throw new ConstraintViolationException("Invalid JSON object for params items", null);
		}

		JsonObject result =service.getCostAfterDiscount(items);
		respObj.setStatus(Constants.success);
		respObj.setHttpStatus(HttpStatus.OK);
		respObj.setHttpStatusCode(HttpStatus.OK.value());
		respObj.setMessage(result.toString());

		return respObj;
	}

}
