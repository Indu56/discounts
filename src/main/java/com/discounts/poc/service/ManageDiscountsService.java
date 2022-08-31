package com.discounts.poc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.discounts.poc.exception.ResourceNotFoundException;
import com.discounts.poc.model.DiscountDetail;
import com.discounts.poc.model.ItemDetail;
import com.discounts.poc.repository.DiscountDetailRepository;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.discounts.poc.Constants;

@Service
public class ManageDiscountsService {

	@Autowired
	DiscountDetailRepository ddRepository;

	@Autowired
	private Validator validator;

	public List<DiscountDetail> addDiscount(List<DiscountDetail> discounts) throws Exception {
		List<DiscountDetail> resp = new ArrayList<DiscountDetail>();
		Set<ConstraintViolation<DiscountDetail>> violations = new HashSet<ConstraintViolation<DiscountDetail>>();

		for (DiscountDetail dd : discounts) {
			StringBuilder sb = new StringBuilder();
			violations = validator.validate(dd);
			String additionalValidations = discountDetailValidations(dd);
			if (!violations.isEmpty()) {
				for (ConstraintViolation<DiscountDetail> constraintViolation : violations) {
					sb.append(constraintViolation.getMessage() + "; ");
				}
			}
			sb.append(additionalValidations);
			if (sb.length() > 0) {
				throw new ConstraintViolationException(sb.toString(), violations);
			}
		}

		try {
			resp = ddRepository.saveAll(discounts);
		} catch (Exception e) {
			throw e;
		}

		return resp;
	}

	public void deleteDiscount(String code) throws Exception {

		try {
			ddRepository.findById(code)
					.orElseThrow(() -> new ResourceNotFoundException("Discount code not found : " + code));
			ddRepository.deleteById(code);
		} catch (Exception e) {
			throw e;
		}

	}

	public JsonObject getCostAfterDiscount(List<ItemDetail> items) throws Exception {

		Set<String> types = new HashSet<String>();
		Map<String, Double> finalCostAFterDiscount = new HashMap<String, Double>();
		JsonObject result = new JsonObject();

		Set<ConstraintViolation<ItemDetail>> violations = new HashSet<ConstraintViolation<ItemDetail>>();

		// Validate Input String
		for (ItemDetail dd : items) {
			violations = validator.validate(dd);
			if (!violations.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (ConstraintViolation<ItemDetail> constraintViolation : violations) {
					sb.append(constraintViolation.getMessage() + "; ");
				}
				throw new ConstraintViolationException(sb.toString(), violations);
			}
		}

		try {
			types = items.stream().map(item -> item.getType()).collect(Collectors.toSet());
			final double totalCost = items.stream()
					.collect(Collectors.summingDouble(item -> (item.getCost() * item.getCount())));

			List<DiscountDetail> discounts = ddRepository.getApplicableDiscountDetails(totalCost, types);
			result.addProperty("totalCost", totalCost);
			result.addProperty("isDiscountApplied", false);

			// Calculate By Cost discount type
			DiscountDetail ddByCost = discounts.stream().filter(discount -> discount.getCategory().equals("by_cost"))
					.findFirst().orElse(null);
			if (ddByCost != null) {
				double cost = items.stream().collect(
						Collectors.summingDouble(item -> ddByCost.getCondition() <= (item.getCost() * item.getCount())
								? (item.getCost() * item.getCount())
										- (item.getCost() * item.getCount() * ddByCost.getPercentage()) / 100
								: item.getCost() * item.getCount()));
				finalCostAFterDiscount.put(ddByCost.getCode(), cost);
			}

			// Calculate By Type discount type
			Map<String, Double> byType = discounts.stream().filter(discount -> discount.getCategory().equals("by_type"))
					.collect(Collectors.toMap(d -> d.getCode(),
							d -> totalCost - ((totalCost * d.getPercentage()) / 100)));
			finalCostAFterDiscount.putAll(byType);

			// Calculate By Count discount type
			List<DiscountDetail> byCount = discounts.stream()
					.filter(discount -> discount.getCategory().equals("by_count")).toList();
			for (DiscountDetail d : byCount) {
				double cost = items.stream()
						.collect(Collectors.summingDouble(
								item -> d.getType().equals(item.getType()) && item.getCount() >= d.getCondition()
										? (item.getCost() * item.getCount())
												- (item.getCost() * item.getCount() * d.getPercentage()) / 100
										: item.getCost() * item.getCount()));
				if (cost != totalCost) {
					finalCostAFterDiscount.put(d.getCode(), cost);
				}
			}

			// Get the most applied discount
			if (!finalCostAFterDiscount.isEmpty()) {
				Entry<String, Double> minCost = Collections.min(finalCostAFterDiscount.entrySet(),
						Comparator.comparing(Entry::getValue));

				result.addProperty("totalCost", minCost.getValue());
				DiscountDetail discountApplied = discounts.stream()
						.filter(discount -> discount.getCode().equals(minCost.getKey())).findFirst().orElse(null);

				result.addProperty("discountApplied", new Gson().toJson(discountApplied, DiscountDetail.class));
				result.addProperty("isDiscountApplied", true);
			}
		} catch (Exception e) {
			throw e;
		}

		return result;

	}

	private String discountDetailValidations(DiscountDetail discount) {
		StringBuilder sb = new StringBuilder();
		if (!Constants.categories.contains(discount.getCategory())) {
			sb.append("Invalid category. Valid values: " + Constants.categories.toString() + "; ");
		}
		if (discount.getType() == null
				&& (discount.getCategory().equals("by_type") || discount.getCategory().equals("by_count"))
				&& discount.getType().equals("")) {
			sb.append("Discount Type is required when category is by_type or by_count; ");
		}
		if ((discount.getCategory().equals("by_cost") || discount.getCategory().equals("by_count"))
				&& discount.getCondition() <= 0) {
			sb.append("Discount Condition must be greater than 0 when category is by_cost or by_count; ");
		}
		return sb.toString();
	}

}
