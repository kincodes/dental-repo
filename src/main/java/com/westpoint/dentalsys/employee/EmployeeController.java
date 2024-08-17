package com.westpoint.dentalsys.employee;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public String add(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        return "New employee added successfully!";
    }

    @GetMapping("/getAll")
    public List<Employee> getAllEmployees(){
      return  employeeService.getAllEmployees();
    }
    @GetMapping("/getRole")
    public List<Employee> getAllEmployeesByRole(){
        return  employeeService.findAllEmployeeByRole();
    }

    @PutMapping("update/{employeeId}")
    public String updateEmployee(@RequestBody Employee employee, @PathVariable int employeeId){

        employeeService.updateEmployee(employee, employeeId);

        return "Employee record updated successfully!";
    }

    @PutMapping("delete/{employeeId}") //set employee inactive
    public String deleteEmployee(@RequestBody Employee employee, @PathVariable int employeeId){

        employeeService.updateEmployee(employee, employeeId);

        return "Employee record deleted!";
    }

}
