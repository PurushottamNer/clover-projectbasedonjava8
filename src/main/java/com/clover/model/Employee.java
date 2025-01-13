package com.clover.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	private String employeeId;
	private String employeeName;
	private int employeeAge;
	private String employeeGender;
	private String employeeDepartment;
	private int employeeYearOfJoining;
	private double employeeSalary;

}
