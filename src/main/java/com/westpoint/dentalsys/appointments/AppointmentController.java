package com.westpoint.dentalsys.appointments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/add")
    public String add(@RequestBody Appointment appointment){
        appointmentService.saveAppointment(appointment);
        return "New appointment added successfully!";
    }

    @GetMapping("/getAll")
    public List<Appointment> getAllAppointments(){
        return  appointmentService.getAllAppointments();
    }
    @GetMapping("/getAppointment/{email}")
    public List<Appointment> getAppointment( @PathVariable String email){
        return  appointmentService.getPatientAppointments(email);
    }

    @PutMapping("/update/{appointmentId}")
    public String updateAppointment(@RequestBody Appointment appointment, @PathVariable int appointmentId){

        appointmentService.updateAppointment(appointment, appointmentId);

        return "Appointment updated successfully!";
    }

    @PutMapping("/delete/{appointmentId}")
    public String deleteAppointment(@RequestBody Appointment appointment, @PathVariable int appointmentId){
        appointmentService.deleteAppointment(appointment, appointmentId);
        return "Appointment updated successfully!";
    }
}
