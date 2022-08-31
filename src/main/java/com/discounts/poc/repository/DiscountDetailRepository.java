package com.discounts.poc.repository;

import org.springframework.stereotype.Repository;

import com.discounts.poc.model.DiscountDetail;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, String> {

	@Query(
		value = "select * from discount_detail where\n"
				+ " (category in ('by_type', 'by_count') and type in (:types))\n"
				+ " or (category = 'by_cost' and condition <= :totalCost) order by category, percentage",
		nativeQuery = true)
	public List<DiscountDetail> getApplicableDiscountDetails(@Param("totalCost") double totalCost, @Param("types") Set<String> types);
}
