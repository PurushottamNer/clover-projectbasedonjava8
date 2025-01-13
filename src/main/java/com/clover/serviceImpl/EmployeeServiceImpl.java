package com.clover.serviceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.dto.EmployeeTenureDTO;
import com.clover.dto.TenureDTO;
import com.clover.model.Employee;
import com.clover.repository.EmployeeRepository;
import com.clover.service.EmployeeService;
import com.clover.utility.EmployeeUtils;
import com.clover.utility.SalaryIncrementStrategy;

@Service
public class EmployeeServiceImpl implements EmployeeService, EmployeeUtils {

	private static final String valueOf = null;
	@Autowired
	private EmployeeRepository employeeRepository;

	public String idGenerator() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	// used the default method to format the employeeId by adding the prefix 'EMP'
	// to the employeeId, where the logic for the prefix is implemented
	@Override
	public List<Employee> addEmployee1(List<Employee> employees) {

		List<Employee> savedEmployees = new ArrayList<>();

		for (Employee emp : employees) {
			boolean isUniqueIdFound = false;
			String uniqueId = "";

			while (!isUniqueIdFound) {
				uniqueId = idGenerator();
				String formattedUniqueId = formatEmployeeId(uniqueId);
				if (employeeRepository.findById(formattedUniqueId).isEmpty()) {
					isUniqueIdFound = true;
					uniqueId = formattedUniqueId;
				}
			}

			emp.setEmployeeId(uniqueId);

			savedEmployees.add(emp);
		}

		return employeeRepository.saveAll(savedEmployees);
	}

	// Using forEach method to iterate over a list for adding Employees
	@Override
	public List<Employee> addEmployee2(List<Employee> employees) {

		List<Employee> savedEmployees = new ArrayList<>();

		employees.forEach(emp -> {
			boolean isUniqueIdFound = false;
			String uniqueId = "";
			while (!isUniqueIdFound) {
				uniqueId = "EMP" + idGenerator();
				if (employeeRepository.findById(uniqueId).isEmpty()) {
					isUniqueIdFound = true;
				}
			}

			emp.setEmployeeId(uniqueId);
			savedEmployees.add(emp);
		});

		return employeeRepository.saveAll(savedEmployees);
	}

	@Override
	public TenureDTO getEmployeeTenure(String employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		int currentYear = Year.now().getValue();
		int tenureYears = EmployeeUtils.calculateEmployeeTenureInYears(employee.getEmployeeYearOfJoining());

		TenureDTO tenureDTO = new TenureDTO(employee.getEmployeeId(), employee.getEmployeeName(),
				employee.getEmployeeYearOfJoining(), currentYear, tenureYears);

		return tenureDTO;
	}

	@Override
	@Transactional
	public List<Employee> salaryIncreamentBasedOnTenure() {
		List<Employee> employees = employeeRepository.findAll();

		List<Employee> updatedEmployees = new ArrayList<>();

		SalaryIncrementStrategy incrementForMoreThan5Years = tenure -> tenure > 5 ? 0.30 : 0.0; // 30% for tenure > 5
																								// years
		SalaryIncrementStrategy incrementForMoreThan3Years = tenure -> tenure > 3 && tenure <= 5 ? 0.10 : 0.0; // 10%
																												// for
																												// tenure
																												// > 3
																												// and
																												// <= 5
																												// years

		employees.forEach(employee -> {
			int tenure = EmployeeUtils.calculateEmployeeTenureInYears(employee.getEmployeeYearOfJoining());

			double incrementPercentage = incrementForMoreThan5Years.calculateIncrement(tenure);

			if (incrementPercentage == 0.0) {
				incrementPercentage = incrementForMoreThan3Years.calculateIncrement(tenure);
			}

			if (incrementPercentage > 0.0) {
				double newSalary = employee.getEmployeeSalary() * (1 + incrementPercentage);
				employee.setEmployeeSalary(newSalary);
				updatedEmployees.add(employee);
			}
		});

		employeeRepository.saveAll(updatedEmployees);
		return updatedEmployees;
	}

	@Override
	public double calculateAverageSalaryForTenure(int minTenure) {
		List<Employee> employees = employeeRepository.findAll();

		return employees.stream()
				.filter(employee -> EmployeeUtils
						.calculateEmployeeTenureInYears(employee.getEmployeeYearOfJoining()) > minTenure)
				.mapToDouble(Employee::getEmployeeSalary).average().orElse(0.0);
	}

	@Override
	public int getTheDaysSinceJoiningDay(String employeeId, int yearOfJoining) {
		int days = 0;
		try {
			Optional<Employee> employee = employeeRepository.findById(employeeId);

			if (!employee.isPresent()) {
				throw new RuntimeException("Employee not found with ID: " + employeeId);
			}

			Employee emp = employee.get();

			if (emp.getEmployeeYearOfJoining() != yearOfJoining) {
				throw new RuntimeException("Employee ID and year of joining do not match.");
			}

			int calculateEmployeeTenureInYears = EmployeeUtils
					.calculateEmployeeTenureInYears(emp.getEmployeeYearOfJoining());

			int daysInYear = getDaysInYear(calculateEmployeeTenureInYears);

			days = calculateEmployeeTenureInYears * daysInYear;

			return days;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return days;

	}

	public static int getDaysInYear(int year) {
		Year givenYear = Year.of(year);

		return givenYear.isLeap() ? 366 : 365;
	}

	@Override
	public Map<String, List<Employee>> groupEmployeesByDepartment() {
		List<Employee> listOfEmployees = employeeRepository.findAll();

		Map<String, List<Employee>> collect = listOfEmployees.stream()
				.collect(Collectors.groupingBy(Employee::getEmployeeDepartment));
		return collect;
	}

	@Override
	public List<EmployeeTenureDTO> fetchEmployeesAndCalculateTenure(List<String> employeeIds) {

		List<CompletableFuture<EmployeeTenureDTO>> futures = employeeIds.stream()
				.map(id -> CompletableFuture.supplyAsync(() -> {
					Optional<Employee> optionalEmployee = employeeRepository.findById(id);
					if (optionalEmployee.isEmpty()) {
						throw new RuntimeException("Employee with ID " + id + " not found.");
					}
					return optionalEmployee.get();
				}).thenApplyAsync(employee -> {
					int tenure = EmployeeUtils.calculateEmployeeTenureInYears(employee.getEmployeeYearOfJoining());
					return new EmployeeTenureDTO(employee.getEmployeeId(), employee.getEmployeeName(), tenure);
				})).collect(Collectors.toList());

		return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

	}

}
