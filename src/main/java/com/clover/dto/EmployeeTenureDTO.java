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
public class EmployeeTenureDTO {

	private String employeeId;
	private String employeeName;
	private int tenure;
}
