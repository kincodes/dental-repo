package com.westpoint.dentalsys.schedule;

import java.util.List;

public interface ScheduleService {

     Schedule saveSchedule(Schedule schedule, int employeeId);

     Schedule updateSchedule(Schedule schedule, int scheduleId);
     List<Schedule> getAllSchedules();
    //public List<Schedule> getSchedulesForAppointment();

     void deleteSchedule(Integer scheduleId);
}
