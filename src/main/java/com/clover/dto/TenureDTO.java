package com.clover.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TenureDTO {
	private String employeeId;
	private String employeeName;
	private int employeeYearOfJoining;
	private int currentYear;
	private int tenureFromYearOfJoining;
}
