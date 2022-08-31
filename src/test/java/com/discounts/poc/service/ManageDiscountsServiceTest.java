package com.discounts.poc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.junit.jupiter.MockitoExtension;

import com.discounts.poc.DiscountsApplication;
import com.discounts.poc.model.DiscountDetail;
import com.discounts.poc.model.ItemDetail;
import com.discounts.poc.repository.DiscountDetailRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DiscountsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ManageDiscountsServiceTest {

	@MockBean
	DiscountDetailRepository ddRepository;

	@InjectMocks
	private ManageDiscountsService manageDiscountsService;

	@MockBean
	private Validator validator;

	@Test
	void addDiscounts() {
		try {
			String str = "[{\"code\":\"code001\",\"category\":\"by_cost\",\"type\":\"\",\"condition\":10,\"percentage\":10},{\"code\":\"code002\",\"category\":\"by_type\",\"type\":\"electronics\",\"condition\":0,\"percentage\":5}]";
			List<DiscountDetail> discount = new Gson().fromJson(str, new TypeToken<ArrayList<DiscountDetail>>() {
			}.getType());
			Mockito.when(validator.validate(discount)).thenReturn(Collections.emptySet());
			Mockito.when(ddRepository.saveAll(discount)).thenReturn(discount);
			List<DiscountDetail> result = manageDiscountsService.addDiscount(discount);
			assertEquals(result.size(), discount.size());
		} catch (Exception e) {
			assertFalse(true);
		}

	}

	@Test
	void getApplicableDiscountsNoDiscountsApplied() {
		try {
			String str = "[{\"id\": 123,\"cost\": 50,\"type\": \"electronics\",\"count\": 1}]";
			List<ItemDetail> items = new Gson().fromJson(str, new TypeToken<ArrayList<ItemDetail>>() {
			}.getType());
			List<DiscountDetail> dd = new ArrayList<DiscountDetail>();
			Mockito.when(validator.validate(items.get(0))).thenReturn(Mockito.anySet());
			Mockito.when(ddRepository.getApplicableDiscountDetails(50, Mockito.anySet())).thenReturn(dd);

			JsonObject jObj = manageDiscountsService.getCostAfterDiscount(items);
			assertEquals(jObj.get("totalCost").getAsInt(), 50);
			assertEquals(jObj.get("isDiscountApplied").getAsBoolean(), false);
		} catch (Exception e) {
			assertFalse(true);
		}

	}
	
	@Test
	void getApplicableDiscountsCase1() {
		try {
			String str = "[{\"id\": 123,\"cost\": 50,\"type\": \"clothes\",\"count\": 1}]";
			List<ItemDetail> items = new Gson().fromJson(str, new TypeToken<ArrayList<ItemDetail>>() {
			}.getType());
			String discountStr = "[{\"code\":\"ABC\",\"category\":\"by_type\",\"type\":\"clothes\",\"condition\":0,\"percentage\":10}]";
			List<DiscountDetail> discount = new Gson().fromJson(discountStr,
					new TypeToken<ArrayList<DiscountDetail>>() {
					}.getType());
			Set<String> stypes = new HashSet<String>();
			stypes.add("clothes");
			Mockito.when(validator.validate(items.get(0))).thenReturn(Collections.emptySet());
			Mockito.when(ddRepository.getApplicableDiscountDetails(50, stypes)).thenReturn(discount);

			JsonObject jObj = manageDiscountsService.getCostAfterDiscount(items);
			assertEquals(jObj.get("totalCost").getAsInt(), 45);
			assertEquals(jObj.get("isDiscountApplied").getAsBoolean(), true);
		} catch (Exception e) {
			assertFalse(true);
		}

	}

	@Test
	void getApplicableDiscountsCase2() {
		try {
			String str = "[{\"id\": 123,\"cost\": 50,\"type\": \"clothes\",\"count\": 5}]";
			List<ItemDetail> items = new Gson().fromJson(str, new TypeToken<ArrayList<ItemDetail>>() {
			}.getType());
			String discountStr = "[{\"code\":\"ABC\",\"category\":\"by_type\",\"type\":\"clothes\",\"condition\":0,\"percentage\":10},{\"code\":\"CDE\",\"category\":\"by_cost\",\"type\":\"\",\"condition\":100,\"percentage\":15},{\"code\":\"FGH\",\"category\":\"by_count\",\"type\":\"clothes\",\"condition\":2,\"percentage\":20}]";
			List<DiscountDetail> discount = new Gson().fromJson(discountStr,
					new TypeToken<ArrayList<DiscountDetail>>() {
					}.getType());
			Set<String> stypes = new HashSet<String>();
			stypes.add("clothes");
			Mockito.when(validator.validate(items.get(0))).thenReturn(Collections.emptySet());
			Mockito.when(ddRepository.getApplicableDiscountDetails(250, stypes)).thenReturn(discount);
			JsonObject jObj = manageDiscountsService.getCostAfterDiscount(items);

			assertEquals(jObj.get("totalCost").getAsInt(), 200);
			assertEquals(jObj.get("isDiscountApplied").getAsBoolean(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}

	}

	@Test
	void getApplicableDiscountsCase3() {
		try {
			String str = "[{\"id\": 123,\"cost\": 50,\"type\": \"clothes\",\"count\": 1},{\"id\": 456,\"cost\": 300,\"type\": \"electronics\",\"count\": 1}]";
			List<ItemDetail> items = new Gson().fromJson(str, new TypeToken<ArrayList<ItemDetail>>() {
			}.getType());
			String discountStr = "[{\"code\":\"ABC\",\"category\":\"by_type\",\"type\":\"clothes\",\"condition\":0,\"percentage\":10},{\"code\":\"CDE\",\"category\":\"by_cost\",\"type\":\"\",\"condition\":100,\"percentage\":15}]";
			List<DiscountDetail> discount = new Gson().fromJson(discountStr,
					new TypeToken<ArrayList<DiscountDetail>>() {
					}.getType());
			Set<String> stypes = new HashSet<String>();
			stypes.add("clothes");
			stypes.add("electronics");
			Mockito.when(validator.validate(items.get(0))).thenReturn(Collections.emptySet());
			Mockito.when(ddRepository.getApplicableDiscountDetails(350, stypes)).thenReturn(discount);
			JsonObject jObj = manageDiscountsService.getCostAfterDiscount(items);

			assertEquals(jObj.get("totalCost").getAsInt(), 305);
			assertEquals(jObj.get("isDiscountApplied").getAsBoolean(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}

	}
}
