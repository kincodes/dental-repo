package com.westpoint.dentalsys.schedule;


import com.westpoint.dentalsys.employee.EmployeeRepository;
import com.westpoint.dentalsys.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;



    public Schedule saveSchedule(Schedule schedule, int employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ApiRequestException(("Employee not found"));

        }

        if (scheduleRepository.existsByEmployeeId(employeeId)) {
            throw new ApiRequestException((
                    "employee with id " + employeeId + " already has a schedule"));
        }

        if (schedule.getPeriodStart() == null) {
            throw new ApiRequestException((
                    "period start date is mandatory"));
        }

        if (schedule.getPeriodEnd() == null) {
            throw new ApiRequestException((
                    "period end date is mandatory"));
        }
        if (Objects.equals(schedule.getFirstName(), "") || schedule.getFirstName() == null) {
            throw new ApiRequestException((
                    "employee firstname is mandatory"));
        }
        if (Objects.equals(schedule.getLastName(), "") || schedule.getLastName() == null) {
            throw new ApiRequestException((
                    "employee lastname is mandatory"));
        }
        if (schedule.getWorkTime() == null) {
            throw new ApiRequestException((
                    "work time is mandatory"));
        }
        if (schedule.getEmployeeId() < 0) {
            throw new ApiRequestException((
                    "valid employee id is mandatory"));
        }


        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Schedule newSchedule, int scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ApiRequestException((
                        "schedule with id " + scheduleId + " does not exists")));

        if (newSchedule.getPeriodStart() != null &&
                !Objects.equals(schedule.getPeriodStart(), newSchedule.getPeriodStart())) {
            schedule.setPeriodStart(newSchedule.getPeriodStart());
        }
        if (newSchedule.getPeriodEnd() != null &&
                !Objects.equals(schedule.getPeriodEnd(), newSchedule.getPeriodEnd())) {
            schedule.setPeriodEnd(newSchedule.getPeriodEnd());
        }

        if (newSchedule.getWorkTime() != null &&
                !Objects.equals(schedule.getWorkTime(), newSchedule.getWorkTime())) {
            schedule.setWorkTime(newSchedule.getWorkTime());
        }

        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public void deleteSchedule(Integer scheduleId) {
        boolean exists = scheduleRepository.existsById(scheduleId);
        if (!exists) {
            throw new ApiRequestException(
                    "schedule with id " + scheduleId + " does not exists");
        }
        scheduleRepository.deleteById(scheduleId);
    }


}
