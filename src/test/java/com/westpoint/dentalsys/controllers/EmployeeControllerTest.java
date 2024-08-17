package com.westpoint.dentalsys.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.westpoint.dentalsys.employee.Employee;
import com.westpoint.dentalsys.employee.EmployeeController;
import com.westpoint.dentalsys.employee.EmployeeRepository;
import com.westpoint.dentalsys.employee.EmployeeServiceImpl;
import com.westpoint.dentalsys.login.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //addFilters disables the spring security
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @MockBean
    private EmployeeRepository employeeRepository;


    @InjectMocks
    private EmployeeController employeeController;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    Employee RECORD_1 = new Employee(101, "Elon", "Musk", "elon@starship.com", "888-897-9875", Role.DENTIST);
    Employee RECORD_2 = new Employee(102, "Pat", "Riley", "patriley@gmail.com", "888-456-9875", Role.HYGIENIST);
    Employee RECORD_3 = new Employee(103, "James", "Brown", "jamesbrown@live.com", "888-358-9875", Role.ADMIN);

    @BeforeEach
    public void setUp() {

        List<Employee> records = new ArrayList<>(List.of(RECORD_1, RECORD_2, RECORD_3));

    }

    @Test
    public void testAddEmployee400BadRequest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/add")
                        .content(objectMapper.writeValueAsString(new Employee(894, "", "Fletcher", null, "506-258-2014", Role.DENTIST)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("is mandatory"));

    }

    @Test
    public void testAddEmployee200OK() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/add")
                        .content(objectMapper.writeValueAsString(new Employee(125, "Robert", "Morgan", "email4@mail.com", "506-258-2014", Role.DENTIST)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testGetAllRecords() throws Exception {
        List<Employee> records = new ArrayList<>(List.of(RECORD_1, RECORD_2, RECORD_3));

        when(employeeRepository.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].firstName", is("James")));
    }

    @Test
    public void testUpdateEmployee400BadRequest() throws Exception {

        int id = 176;
        Employee updatedEmployee = new Employee(id, "firstName2", "lastName2", "email2@mail.com", "506-258-2014", Role.DENTIST);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/update/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));


    }


    @Test
    public void testUpdateEmployee200OK() throws Exception {
        int id = 115;

        Employee employee1 = new Employee(id, "Tim", "Brown", "email2@mail.com", "506-258-2014", Role.DENTIST);

        Employee updatedEmployee = new Employee(id, "firstName2", "lastName2", "email2@mail.com", "506-258-2014", Role.DENTIST);


        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/update/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Employee record updated successfully!");

    }

    @Test
    public void testDeleteEmployee400BadRequest() throws Exception {
        int id = 186;

        Employee deleteEmployee = new Employee(id, "Johnny", "Cash", "email5@mail.com", "506-258-2014", Role.HYGIENIST);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/delete/{id}", id)
                        .content(objectMapper.writeValueAsString(deleteEmployee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();


        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));

    }

    @Test
    public void testDeleteEmployee200OK() throws Exception {
        int id = 186;

        Employee employee1 = new Employee(id, "Johnny", "Cash", "email5@mail.com", "506-258-2014", Role.HYGIENIST);

        Employee deleteEmployee = new Employee(id, "Johnny", "Cash", "email5@mail.com", "506-258-2014", Role.HYGIENIST);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenReturn(deleteEmployee);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/delete/{id}", id)
                        .content(objectMapper.writeValueAsString(deleteEmployee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Employee record deleted!");
    }

    @AfterEach
    public void cleanUp() {
        reset(employeeRepository);

    }


}
