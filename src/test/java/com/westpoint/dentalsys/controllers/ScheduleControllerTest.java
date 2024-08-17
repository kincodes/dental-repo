package com.westpoint.dentalsys.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.westpoint.dentalsys.schedule.Schedule;
import com.westpoint.dentalsys.schedule.ScheduleController;
import com.westpoint.dentalsys.schedule.ScheduleRepository;
import com.westpoint.dentalsys.schedule.ScheduleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //addFilters disables the spring security
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder; //assist with date parsing

    @MockBean
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleController scheduleController;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

   // @MockBean
    //ScheduleServiceImpl scheduleService2;

    private List<String> workTimes1 = new ArrayList<>(List.of("9:00 AM", "10:00 AM", "12:00 PM", "1:00 PM", "3:00 PM"));
    private List<String> workTimes2 = new ArrayList<>(List.of("9:00 AM", "10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM"));
    private List<String> workTimes3 = new ArrayList<>(List.of("9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "4:00 PM"));

    Schedule RECORD_1 = new Schedule(101, LocalDate.parse("2024-07-11"), LocalDate.parse("2024-12-11"), "Henry", "Ford",
                    workTimes1, 4);

    Schedule RECORD_2 = new Schedule(101, LocalDate.parse("2024-06-11"), LocalDate.parse("2024-11-11"), "Moya", "Leblanc",
            workTimes2, 4);

    Schedule RECORD_3 = new Schedule(101, LocalDate.parse("2024-05-11"), LocalDate.parse("2024-08-11"), "", "Reid",
            workTimes3, 4);


    @BeforeEach
    public void setUp() {

        List<Schedule> records = new ArrayList<>(List.of(RECORD_1, RECORD_2, RECORD_3));

    }

    @Test
    public void testAddSchedule400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/schedule/add/{employeeId}", RECORD_3.getEmployeeId())
                        .content(newMapper.writeValueAsString(RECORD_3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();


        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("is mandatory"));
    }

    @Test
    public void testAddSchedule200OK() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/schedule/add/{employeeId}", RECORD_2.getEmployeeId())
                        .content(newMapper.writeValueAsString(RECORD_2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testGetAllRecords200OK() throws Exception {
        List<Schedule> recordsLocal = new ArrayList<>(List.of(RECORD_1, RECORD_2));

        when(scheduleRepository.findAll()).thenReturn(recordsLocal);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/schedule/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].firstName", is("Moya")))
                .andExpect(jsonPath("$[1].lastName", is("Leblanc")));


    }

    @Test
    public void testUpdateSchedule400BadRequest()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10;

        List<String> newWorkTimes = new ArrayList<>(List.of("9:00 AM", "10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM"));

        Schedule updatedSchedule = new Schedule(id, LocalDate.parse("2024-07-11"), LocalDate.parse("2024-12-11"), "Douglas", "James",
                newWorkTimes, 5);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders
                        .put("/schedule/update/{scheduleId}", id)
                        .content(newMapper.writeValueAsString(updatedSchedule))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();


        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));

    }

    @Test
    public void testUpdateSchedule200OK()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 10;

        List<String> currentWorkTimes= new ArrayList<>(List.of("8:00", "9:00 AM", "10:00 AM", "12:00 PM", "1:00 PM", "3:00 PM"));
        List<String> newWorkTimes = new ArrayList<>(List.of("9:00 AM", "10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM"));

        Schedule currentSchedule = new Schedule(id, LocalDate.parse("2024-07-11"), LocalDate.parse("2024-12-11"), "Douglas", "James",
                currentWorkTimes, 5);

        Schedule updatedSchedule = new Schedule(id, LocalDate.parse("2024-07-11"), LocalDate.parse("2024-12-11"), "Douglas", "James",
                newWorkTimes, 5);

        when(scheduleRepository.findById(id)).thenReturn(Optional.of(currentSchedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(updatedSchedule);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/schedule/update/{scheduleId}", id)
                        .content(newMapper.writeValueAsString(updatedSchedule))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("updated successfully!"));

    }

   @Disabled
   @Test
    public void testDeleteSchedule200OK()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 121;
        int empId = 5;

        List<String> currentWorkTimes= new ArrayList<>(List.of("8:00", "9:00 AM", "10:00 AM", "12:00 PM", "1:00 PM", "3:00 PM"));

        Schedule currentSchedule = new Schedule(id, LocalDate.parse("2024-07-11"), LocalDate.parse("2024-12-11"), "Douglas", "James",
                currentWorkTimes, empId);

        doNothing().when(scheduleService).deleteSchedule(id);
        //doNothing().when(scheduleRepository).deleteById(id);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/schedule/delete/{scheduleId}", id)
                .content(newMapper.writeValueAsString(currentSchedule))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("schedule deleted!"));

    }

     @Test
    public void testDeleteSchedule400BadRequest()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 121;
        int empId = 5;

        List<String> currentWorkTimes= new ArrayList<>(List.of("8:00", "9:00 AM", "10:00 AM", "12:00 PM", "1:00 PM", "3:00 PM"));

        Schedule currentSchedule = new Schedule(id, LocalDate.parse("2024-07-11"), LocalDate.parse("2024-12-11"), "Douglas", "James",
                currentWorkTimes, empId);

         MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                     .delete("/schedule/delete/{scheduleId}", id)
                                     .content(newMapper.writeValueAsString(currentSchedule))
                                     .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isBadRequest())
                 .andExpect(jsonPath("$", notNullValue()))
                 .andReturn();

         String content = mvcResult.getResponse().getContentAsString();
         assertTrue(content.contains("does not exists"));
    }

    @AfterEach
    public void cleanUp(){
        reset(scheduleRepository);

    }


}
