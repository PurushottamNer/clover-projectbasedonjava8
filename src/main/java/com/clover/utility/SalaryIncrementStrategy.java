package com.clover.utility;

@FunctionalInterface
public interface SalaryIncrementStrategy {

	double calculateIncrement(int tenure);

}
