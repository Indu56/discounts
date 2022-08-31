package com.discounts.poc.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.google.gson.Gson;

public class ItemDetail {

	@NotBlank(message = "Item Id is Required")
	private String id;
	
	@Min(value = 1, message = "Item cost is not valid. Must be greater than 1 or equal to 1")
	private double cost;
	
	@NotBlank(message = "Item Type is Required")
	private String type; 
	
	@Min(value = 1, message = "Invalid Count. Numeric. Must be greater than 0")
	private int count;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
