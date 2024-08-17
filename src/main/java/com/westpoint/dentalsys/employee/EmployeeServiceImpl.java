package com.westpoint.dentalsys.employee;

import com.westpoint.dentalsys.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        if(employeeRepository.existsByEmail(employee.getEmail())){
            throw  new ApiRequestException((
                    "employee with email " + employee.getEmail() + " already exists"));
        }
        if(Objects.equals(employee.getFirstName(), "") || employee.getFirstName() == null){
            throw new ApiRequestException((
                    "firstname is mandatory"));
        }
        if(Objects.equals(employee.getLastName(), "") || employee.getLastName() == null){
            throw new ApiRequestException((
                    "lastname is mandatory"));
        }
        if(Objects.equals(employee.getEmail(), "") || employee.getEmail()  == null){
            throw new ApiRequestException((
                    "email is mandatory"));
        }
        if(Objects.equals(employee.getPhone(), "") || employee.getPhone() == null){
            throw new ApiRequestException((
                    "phone is mandatory"));
        }
        if(employee.getRole() == null){
            throw new ApiRequestException((
                    "role is mandatory"));
        }

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employeeUpdate, int employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiRequestException((
                        "employee with id " + employeeId + " does not exists")));

        if (employeeUpdate.getFirstName() != null &&
                !Objects.equals(employee.getFirstName(), employeeUpdate.getFirstName())){
            employee.setFirstName(employeeUpdate.getFirstName());
        }
        if (employeeUpdate.getLastName() != null &&
                !Objects.equals(employee.getLastName(), employeeUpdate.getLastName())){
            employee.setLastName(employeeUpdate.getLastName());
        }
        if (employeeUpdate.getEmail() != null &&
                !Objects.equals(employee.getEmail(), employeeUpdate.getEmail())){
            employee.setEmail(employeeUpdate.getEmail());
        }
        if (employeeUpdate.getPhone() != null &&
                !Objects.equals(employee.getPhone(), employeeUpdate.getPhone())){
            employee.setPhone(employeeUpdate.getPhone());
        }
        if (employeeUpdate.getRole() != null &&
                !Objects.equals(employee.getRole(), employeeUpdate.getRole())){
            employee.setRole(employeeUpdate.getRole());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployeeByRole() {

        List<Employee> validRoles = employeeRepository.findAllEmployeeByRole();

        if (validRoles.isEmpty()) {
          throw new ApiRequestException((
                    "employees for schedule are empty"));
        }

        return validRoles;
    }


    public Employee deleteEmployee(Employee employeeUpdate, int employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiRequestException((
                        "employee with id " + employeeId + " does not exists")));

        if (employeeUpdate.getFirstName() != null &&
                !Objects.equals(employee.getFirstName(), employeeUpdate.getFirstName())){
            employee.setFirstName(employeeUpdate.getFirstName());
        }
        if (employeeUpdate.getLastName() != null &&
                !Objects.equals(employee.getLastName(), employeeUpdate.getLastName())){
            employee.setLastName(employeeUpdate.getLastName());
        }
        if (employeeUpdate.getEmail() != null &&
                !Objects.equals(employee.getEmail(), employeeUpdate.getEmail())){
            employee.setEmail(employeeUpdate.getEmail());
        }
        if (employeeUpdate.getPhone() != null &&
                !Objects.equals(employee.getPhone(), employeeUpdate.getPhone())){
            employee.setPhone(employeeUpdate.getPhone());
        }
        if (employeeUpdate.getRole() != null &&
                !Objects.equals(employee.getRole(), employeeUpdate.getRole())){
            employee.setRole(employeeUpdate.getRole());
        }

        return employeeRepository.save(employee);
    }



    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


}
