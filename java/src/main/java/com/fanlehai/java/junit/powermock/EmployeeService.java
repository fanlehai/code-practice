package com.fanlehai.java.junit.powermock;

public class EmployeeService {

	public  Employee findEmployeeByEmail(String email) {
		//throw new UnsupportedOperationException();
		return new Employee();
	}

	public  boolean employeeExists(Employee employee) {
		//throw new UnsupportedOperationException();
		return false;
	}
	
	public int getProjectedEmployeeCount(){
		return 100;
	}
	
	public void saveEmployee(Employee employee) {
        if(employee.isNew()) {
            createEmployee(employee);
            return;
        }
        employee.update();
    }  
    
    void createEmployee(Employee employee) {
        //employee.setEmployeeId(EmployeeIdGenerator.getNextId());
        //employee.create();
        //WelcomeEmail emailSender = new WelcomeEmail(employee,
        //"Welcome to Mocking with PowerMock How-to!");
        //emailSender.send();
    }

}
