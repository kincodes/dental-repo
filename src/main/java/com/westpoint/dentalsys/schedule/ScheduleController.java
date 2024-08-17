package com.westpoint.dentalsys.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/add/{employeeId}")
    public String add(@RequestBody Schedule schedule, @PathVariable int employeeId) {
        scheduleService.saveSchedule(schedule, employeeId);
        return "New schedule added successfully!";
    }

    @GetMapping("/getAll")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PutMapping("update/{scheduleId}")
    public String updateSchedule(@RequestBody Schedule schedule, @PathVariable int scheduleId) {

        scheduleService.updateSchedule(schedule, scheduleId);

        return "Schedule updated successfully!";
    }


    @DeleteMapping("delete/{scheduleId}")
    public String deleteSchedule(@PathVariable("scheduleId") Integer scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return "Employee schedule deleted!";
    }

}
