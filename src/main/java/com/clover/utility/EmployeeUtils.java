package com.clover.utility;

import java.time.Year;

public interface EmployeeUtils {

	default String formatEmployeeId(String id) {
		return "EMP" + id;
	}

	static int calculateEmployeeTenureInYears(int yearOfJoining) {
		int currentYear = Year.now().getValue();
		return currentYear - yearOfJoining;
	}

}
