package com.clover.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
