package com.westpoint.dentalsys.employee;


import java.util.List;

public interface EmployeeService {
     Employee saveEmployee(Employee employee);
     List<Employee> getAllEmployees();

     Employee updateEmployee(Employee employee, int employeeId);

     List<Employee> findAllEmployeeByRole();

     //Employee deleteEmployee(Employee employee, int employeeId);









}
