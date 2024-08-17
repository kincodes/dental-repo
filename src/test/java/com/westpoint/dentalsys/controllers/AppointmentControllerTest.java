package com.westpoint.dentalsys.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.westpoint.dentalsys.appointments.Appointment;
import com.westpoint.dentalsys.appointments.AppointmentController;
import com.westpoint.dentalsys.appointments.AppointmentRepository;
import com.westpoint.dentalsys.appointments.AppointmentServiceImpl;
import com.westpoint.dentalsys.patient.PatientRecord;
import com.westpoint.dentalsys.patient.PatientRepository;
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
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder; //assist with date parsing

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentController appointmentController;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;


    Appointment RECORD_1 = new Appointment(111, 1, "Gregory", LocalDate.parse("2024-06-28"), "9:00 AM", 1, "Dawson", "Cleaning", "Right tooth sensitive", "BOOKED");
    Appointment RECORD_2 = new Appointment(123, 2, "", LocalDate.parse("2024-07-11"), "10:00 AM", 3, "Kelly", "Whitening", "Room 5", "BOOKED");

    Appointment RECORD_3 = new Appointment(156, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "BOOKED");
    Appointment RECORD_3_Copy = new Appointment(186, 6, "Megan", LocalDate.parse("2024-10-17"), "12:00 PM", 5, "Dawson", "Filling", "Room 7", "BOOKED");


    PatientRecord PATIENT_RECORD = new PatientRecord(6, "Megan", "Jamie", LocalDate.parse("1989-07-11"), "jamiemiller@gmail.com", "886-899-5898",
            "123 Main Street", "Crow Town", "NB", "E4J 9I9", "F", "TD", "0000A1245", "John Brown", "886-789-1489",
            "Spouse", 0.0, "ACTIVE");


    @BeforeEach
    public void setUp() {

        List<Appointment> records = new ArrayList<>(List.of(RECORD_1, RECORD_2, RECORD_3));

    }

    @Test
    public void testAddAppointment400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/appointment/add")
                        .content(newMapper.writeValueAsString(RECORD_2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("is mandatory"));

    }

    @Test
    public void testAddAppointment200OK() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/appointment/add")
                        .content(newMapper.writeValueAsString(RECORD_1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testGetAllRecords200OK() throws Exception {
        List<Appointment> recordsLocal = new ArrayList<>(List.of(RECORD_1, RECORD_3));

        when(appointmentRepository.findAll()).thenReturn(recordsLocal);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/appointment/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].patientName", is("Megan")));


    }

    @Test
    public void testGetAppointmentsByPatient200OK() throws Exception {
        String email = "jamiemiller@gmail.com";
        List<Appointment> records = new ArrayList<>(List.of(RECORD_3, RECORD_3_Copy));

        Appointment RECORD_NEW = new Appointment(156, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "BOOKED");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/appointment/getAppointment/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAppointment400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 186;

        Appointment updatedAppointment = new Appointment(id, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "COMPLETED");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/appointment/update/{id}", id)
                        .content(newMapper.writeValueAsString(updatedAppointment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();


        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));

    }

    @Test
    public void testUpdateAppointment200OK() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 186;

        Appointment currentAppointment = new Appointment(id, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "BOOKED");

        Appointment updatedAppointment = new Appointment(id, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "COMPLETED");

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(currentAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/appointment/update/{id}", id)
                        .content(newMapper.writeValueAsString(updatedAppointment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("updated successfully!"));

    }

    @Test
    public void testDeleteAppointment400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10001;


        Appointment updatedAppointment = new Appointment(id, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "CANCELLED");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/appointment/delete/{id}", id)
                        .content(newMapper.writeValueAsString(updatedAppointment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));


    }

    @Test
    public void testDeleteAppointment200OK() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10001;

        Appointment currentAppointment = new Appointment(id, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "BOOKED");

        Appointment updatedAppointment = new Appointment(id, 6, "Megan", LocalDate.parse("2024-07-17"), "11:00 AM", 5, "Dawson", "Cleaning", "Room 7", "CANCELLED");

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(currentAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/appointment/delete/{id}", id)
                        .content(newMapper.writeValueAsString(updatedAppointment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("updated successfully!"));

    }

    @AfterEach
    public void cleanUp() {

        reset(appointmentRepository);

    }


}
