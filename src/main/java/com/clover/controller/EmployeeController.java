package com.clover.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.dto.EmployeeTenureDTO;
import com.clover.dto.TenureDTO;
import com.clover.model.Employee;
import com.clover.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/java8")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/addEmployee")
	public ResponseEntity<?> addEmployee1(@RequestBody List<Employee> listOfEmployees) {
		try {
			List<Employee> employees = employeeService.addEmployee1(listOfEmployees);
			return ResponseEntity.ok(employees);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to add employees: " + e.getMessage());
		}
	}

	@PostMapping("/addEmployeeUsingForEach")
	public ResponseEntity<?> addEmployee2(@RequestBody List<Employee> listOfEmployees) {
		try {
			List<Employee> employees = employeeService.addEmployee2(listOfEmployees);
			return ResponseEntity.ok(employees);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to add employees: " + e.getMessage());
		}
	}

	@GetMapping("/getEmployeeTenureByUsingStaticMethod")
	public ResponseEntity<TenureDTO> getEmployeeTenureByUsingStaticMethod(@RequestParam String employeeId) {
		TenureDTO employeeTenure = employeeService.getEmployeeTenure(employeeId);
		return ResponseEntity.ok(employeeTenure);
	}

	@PutMapping("/salaryIncreament")
	public ResponseEntity<List<Employee>> salaryIncreamentForEmployees() {
		List<Employee> updatedEmployees = employeeService.salaryIncreamentBasedOnTenure();
		return ResponseEntity.ok(updatedEmployees);
	}

	@GetMapping("/averageSalaryMoreThanYears")
	public ResponseEntity<Double> getAverageSalaryForGivenTenure(@RequestParam int tenure) {
		double averageSalary = employeeService.calculateAverageSalaryForTenure(tenure);

		return ResponseEntity.ok(averageSalary);
	}

	@GetMapping("/getTheDaysSinceJoiningDay")
	public int getTheDaysSinceJoiningDay(@RequestParam String employeeId, @RequestParam int yearOfJoining) {
		return employeeService.getTheDaysSinceJoiningDay(employeeId, yearOfJoining);

	}

	@GetMapping("/groupEmployeesByDepartment")
	public ResponseEntity<Map<String, List<Employee>>> getEmployeesGroupedByDepartment() {
		try {
			Map<String, List<Employee>> groupedEmployees = employeeService.groupEmployeesByDepartment();
			return ResponseEntity.ok(groupedEmployees);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/calculateTenureByCompletableFuture")
	public ResponseEntity<List<EmployeeTenureDTO>> getEmployeesWithTenure(@RequestBody List<String> employeeIds) {
		List<EmployeeTenureDTO> employeesWithTenure = employeeService.fetchEmployeesAndCalculateTenure(employeeIds);
		return ResponseEntity.ok(employeesWithTenure);
	}

}
