package com.westpoint.dentalsys.appointments;

import java.util.List;

public interface AppointmentService {

     Appointment saveAppointment(Appointment appointment);

     Appointment updateAppointment(Appointment appointment, int appointmentId);
     Appointment deleteAppointment(Appointment appointment, int appointmentId);
     List<Appointment> getAllAppointments();

     List<Appointment> getPatientAppointments(String patientId);
    //public List<Appointment> getPatientAppointments(Integer patientId);

}
