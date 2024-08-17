package com.westpoint.dentalsys.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.westpoint.dentalsys.patient.PatientRecord;
import com.westpoint.dentalsys.patient.PatientRepository;
import com.westpoint.dentalsys.patient.PatientServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //addFilters disables the spring security

public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder; //assist with date parsing


    @MockBean
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;


    PatientRecord RECORD_1 = new PatientRecord(6, "Megan", "Jamie", LocalDate.parse("1989-07-11"), "jamiemiller@gmail.com", "886-899-5898",
            "123 Main Street", "Crow Town", "NB", "E4J 9I9", "F", "TD", "0000A1245", "John Brown", "886-789-1489",
            "Spouse", 0.0, "ACTIVE");

    PatientRecord RECORD_2 = new PatientRecord(95, "", "Brown", LocalDate.parse("1995-05-02"), "", "886-987-5898",
            "123 Main Street", "Slip Town", "NB", "E4J 9I9", "M", "TD", "0000A1245", "Melisa Brown", "878-789-1489",
            "Wife", 0.0, "ACTIVE");

    PatientRecord RECORD_3 = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
            "555 Crown Rd", "Frank Town", "NB", "E4J 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
            "Sister", 0.0, "ACTIVE");

    @BeforeEach
    public void setUp() {

       // List<PatientRecord> records = new ArrayList<>(List.of(RECORD_1, RECORD_2, RECORD_3));
    }

    @Test
    public void testAddPatient400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/patient/add")
                        .content(newMapper.writeValueAsString( RECORD_2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("is mandatory"));
    }

    @Test
    public void testAddPatient200OK() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/patient/add")
                        .content(newMapper.writeValueAsString( RECORD_3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testGetAllPatients200OK() throws Exception {
        List<PatientRecord> recordsLocal = new ArrayList<>(List.of(RECORD_1, RECORD_3));

        when(patientRepository.findAll()).thenReturn(recordsLocal);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/patient/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].firstName", is("Amy")));


    }

    @Test
    public void testUpdatePatient400BadRequest()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10005;

        PatientRecord updatedPatient = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
                "12 Martin Road", "Frank Town", "NB", "E7Y 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
                "Sister", 0.0, "ACTIVE");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/patient/update/{id}", id)
                        .content(newMapper.writeValueAsString(updatedPatient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));

    }


    @Test
    public void testUpdatePatient200OK()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10005;

        PatientRecord currentPatient = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
                "555 Crown Rd", "Frank Town", "NB", "E4J 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
                "Sister", 0.0, "ACTIVE");


        PatientRecord updatedPatient = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
                "12 Martin Road", "Frank Town", "NB", "E7Y 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
                "Sister", 0.0, "ACTIVE");



        when(patientRepository.findById(id)).thenReturn(Optional.of(currentPatient));
        when(patientRepository.save(any(PatientRecord.class))).thenReturn(updatedPatient);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/patient/update/{id}", id)
                        .content(newMapper.writeValueAsString(updatedPatient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("updated successfully!"));

    }

    @Test
    public void testDeletePatient400BadRequest()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 98847;


        PatientRecord deletePatient = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
                "12 Martin Road", "Frank Town", "NB", "E7Y 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
                "Sister", 0.0, "INACTIVE"); // setting patient inactive


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                    .put("/patient/delete/{id}", id)
                                    .content(newMapper.writeValueAsString(deletePatient))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));

    }


    @Test
    public void testDeletePatient200OK()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10059;

        PatientRecord currentPatient = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
                "555 Crown Rd", "Frank Town", "NB", "E4J 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
                "Sister", 0.0, "ACTIVE");


        PatientRecord deletePatient = new PatientRecord(102, "Amy", "Richards", LocalDate.parse("1994-11-11"), "amyrichards@gmail.com", "905-899-5898",
                "12 Martin Road", "Frank Town", "NB", "E7Y 9I9", "F", "TD", "000AA1245", "Ann Richards", "967-789-1489",
                "Sister", 0.0, "INACTIVE"); // setting patient inactive



        when(patientRepository.findById(id)).thenReturn(Optional.of(currentPatient));
        when(patientRepository.save(any(PatientRecord.class))).thenReturn(deletePatient);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/patient/delete/{id}", id)
                        .content(newMapper.writeValueAsString(deletePatient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("record deleted!"));

    }
    @AfterEach
    public void cleanUp(){
        reset(patientRepository);

    }




}
