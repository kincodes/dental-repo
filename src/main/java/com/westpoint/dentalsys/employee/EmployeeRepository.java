package com.westpoint.dentalsys.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("""
            select t from Employee t where t.role = DENTIST or t.role = HYGIENIST
            """)
    List<Employee> findAllEmployeeByRole(); //select employee to add their schedules

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);




}
