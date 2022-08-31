package com.discounts.poc;

import java.util.Arrays;
import java.util.HashSet;

public class Constants {
	
	public static HashSet<String> categories = new HashSet<String>(
			Arrays.asList(new String[] { "by_type", "by_count", "by_cost" }));
	public static final String success = "Success";
	public static final String failure = "Failure";


}
