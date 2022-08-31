package com.discounts.poc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import com.google.gson.Gson;


@Entity
@Table(name = "discountDetail")
public class DiscountDetail {
	
	@NotBlank(message = "Discount Code is Required")
	private String code;
	
	@NotNull(message = "Discount Category is Required")
	private String category;
		
	private String type = "";
	
	private int condition;
	
	@Min(value = 1, message = "Invalid percentage. Numeric. Must be greater than 0 and less than or equal to 100")
	@Max(value = 100, message = "Invalid percentage. Numeric. Must be greater than 0 and less than or equal to 100")
	private int percentage;
	
	private String createdBy = "system";
	private Date createdDate = new Date();

	@Id
	@Column(unique = true)
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "category", nullable = false)
	public String getCategory() {
		return this.category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Column(name = "type", nullable = false)
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "condition", nullable = false)
	public int getCondition() {
		return this.condition;
	}
	
	public void setCondition(int condition) {
		this.condition = condition;
	}
	
	@Column(name = "percentage", nullable = false)
	public int getPercentage() {
		return this.percentage;
	}
	
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	@Column(name = "created_by", nullable = false)
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "created_date", nullable = false)
	@CreatedDate
	public Date getCreatedDate() {
		return this.createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
