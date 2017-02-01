package com.fanlehai.java.junit.powermock;

public class EmployeeController {

	EmployeeService employeeService;
	EmployeeController(EmployeeService es){
		employeeService = es;
	}
	
	public Employee findEmployeeByEmail(String email) {
		return employeeService.findEmployeeByEmail(email);
	}

	public boolean isEmployeeEmailAlreadyTaken(String email) {
		Employee employee = new Employee();
		return employeeService.employeeExists(employee);
	}

	public int getProjectedEmployeeCount(){
		return employeeService.getProjectedEmployeeCount();
	}
}
