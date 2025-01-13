package com.clover.service;

import java.util.List;
import java.util.Map;

import com.clover.dto.EmployeeTenureDTO;
import com.clover.dto.TenureDTO;
import com.clover.model.Employee;

public interface EmployeeService {

	public List<Employee> addEmployee1(List<Employee> employee);

	public List<Employee> addEmployee2(List<Employee> employee);

	public TenureDTO getEmployeeTenure(String employeeId);

	public List<Employee> salaryIncreamentBasedOnTenure();

	public double calculateAverageSalaryForTenure(int minTenure);

	public int getTheDaysSinceJoiningDay(String employeeId, int yearOfJoining);

	public Map<String, List<Employee>> groupEmployeesByDepartment();

	public List<EmployeeTenureDTO> fetchEmployeesAndCalculateTenure(List<String> employeeIds);

}
