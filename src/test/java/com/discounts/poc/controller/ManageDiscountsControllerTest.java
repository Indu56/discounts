package com.discounts.poc.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.discounts.poc.DiscountsApplication;
import com.discounts.poc.model.DiscountDetail;
import com.discounts.poc.model.ResponseObject;
import com.discounts.poc.repository.DiscountDetailRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@ExtendWith(SpringExtension.class)	
@SpringBootTest(classes = DiscountsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ManageDiscountsControllerTest {

	private final String ADD_DISCOUNTS_URL = "/api/discounts/v1/";
	private final String DELETE_DISCOUNTS_URL = "/api/discounts/v1/D001";
	private final String GET_APPLICABLE_DISCOUNTS_URL = "/api/discounts/v1/get-best-applicable-discount";


	@MockBean
	DiscountDetailRepository ddRepository;
	
	@Autowired
	private transient TestRestTemplate restTemplate;

	private transient HttpHeaders headers;
	private transient HttpEntity<?> entity;

	@Test
	public void testJunit() {
		String check = "success";
		assertThat(check).isEqualTo("success");
	}

	@Test
	void testAddDiscountSuccess() {
		try {
			headers = new HttpHeaders();
			String bodyParam = "[{\"code\":\"code001\",\"category\":\"by_cost\",\"type\":\"\",\"condition\":10,\"percentage\":10},{\"code\":\"code002\",\"category\":\"by_type\",\"type\":\"electronics\",\"condition\":0,\"percentage\":5}]";
			List<DiscountDetail> discount = new Gson().fromJson(bodyParam, new TypeToken<ArrayList<DiscountDetail>>() {
			}.getType());
			entity = new HttpEntity<Object>(discount, headers);
			
			//Mockito.when(manageDiscountsService.addDiscount(discount)).thenReturn(null);
			Mockito.when(ddRepository.saveAll(discount)).thenReturn(null);
			final ResponseEntity<ResponseObject> response = this.restTemplate.exchange(ADD_DISCOUNTS_URL,
					HttpMethod.POST, entity, ResponseObject.class);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		} catch (Exception e) {
			assertFalse(true);
		}
	}
	
	@Test
	void testAddDiscountValidationFailure() {
		try {
			headers = new HttpHeaders();
			String bodyParam = "[{\"code\":\"code001\",\"category\":\"by_co\",\"type\":\"\",\"condition\":10,\"percentage\":10},{\"code\":\"code002\",\"category\":\"by_type\",\"type\":\"electronics\",\"condition\":0,\"percentage\":5}]";

			List<DiscountDetail> discount = new Gson().fromJson(bodyParam, new TypeToken<ArrayList<DiscountDetail>>() {
			}.getType());
			entity = new HttpEntity<Object>(discount, headers);
			
			//Mockito.when(manageDiscountsService.addDiscount(discount)).thenReturn(null);
			Mockito.when(ddRepository.saveAll(discount)).thenReturn(null);
			final ResponseEntity<ResponseObject> response = this.restTemplate.exchange(ADD_DISCOUNTS_URL,
					HttpMethod.POST, entity, ResponseObject.class);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			assertFalse(true);
		}
	}

	@Test
	void testDeleteDiscountSuccess() {
		try {
			headers = new HttpHeaders();
			entity = new HttpEntity<Object>(headers);
			DiscountDetail dd = new DiscountDetail();
			Optional<DiscountDetail> opt = Optional.of(dd);
			Mockito.when(ddRepository.findById(Mockito.anyString())).thenReturn(opt);
			final ResponseEntity<ResponseObject> response = this.restTemplate.exchange(DELETE_DISCOUNTS_URL,
					HttpMethod.DELETE, entity, ResponseObject.class);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		} catch (Exception e) {
			assertFalse(true);
		}
	}
	
	@Test
	void testDeleteDiscountFailure() {
		try {
			headers = new HttpHeaders();
			entity = new HttpEntity<Object>(headers);			
			Optional<DiscountDetail> opt = Optional.empty();
			Mockito.when(ddRepository.findById(Mockito.anyString())).thenReturn(opt);
			final ResponseEntity<ResponseObject> response = this.restTemplate.exchange(DELETE_DISCOUNTS_URL,
					HttpMethod.DELETE, entity, ResponseObject.class);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			assertFalse(true);
		}
	}
	
	@Test
	void testGetBestApplicableDiscountSuccess() {
		try {
			headers = new HttpHeaders();
			String param = "[{\"id\": 123,\"cost\": 50,\"type\": \"electronics\",\"count\": 1}]";
			headers.set("items", param);
			entity = new HttpEntity<Object>(headers);
			List<DiscountDetail> dd = new ArrayList<DiscountDetail>();
			Mockito.when(ddRepository.getApplicableDiscountDetails(Mockito.anyDouble(), Mockito.anySet())).thenReturn(dd);
			final ResponseEntity<ResponseObject> response = this.restTemplate.exchange(GET_APPLICABLE_DISCOUNTS_URL,
					HttpMethod.GET, entity, ResponseObject.class);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	void testGetBestApplicableDiscountValidationFailure() {
		try {
			headers = new HttpHeaders();
			String param = "[{\"id\": 123,\"type\": \"electronics\",\"count\": 1}]";
			headers.set("items", param);
			entity = new HttpEntity<Object>(headers);			
			List<DiscountDetail> dd = new ArrayList<DiscountDetail>();
			Mockito.when(ddRepository.getApplicableDiscountDetails(Mockito.anyDouble(), Mockito.anySet())).thenReturn(dd);
			final ResponseEntity<ResponseObject> response = this.restTemplate.exchange(GET_APPLICABLE_DISCOUNTS_URL,
					HttpMethod.GET, entity, ResponseObject.class);
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
}
