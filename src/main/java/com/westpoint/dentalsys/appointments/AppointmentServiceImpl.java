package com.westpoint.dentalsys.appointments;

import com.westpoint.dentalsys.employee.EmployeeRepository;
import com.westpoint.dentalsys.employee.EmployeeServiceImpl;
import com.westpoint.dentalsys.exception.ApiRequestException;
import com.westpoint.dentalsys.patient.PatientRecord;
import com.westpoint.dentalsys.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private PatientRepository patientRepository;



    public Appointment saveAppointment(Appointment appointment) {
       /* if(!employeeRepository.existsByEmployeeId(appointment.getEmployeeId())){
            throw  new ApiRequestException((
                    "employee with email " + appointment.getEmployeeId() + " already exists"));

        }*/
        if(appointment.getPatientId() == 0 ){
            throw new ApiRequestException((
                    "patient id is mandatory"));
        }

        if(Objects.equals(appointment.getPatientName(), "") || appointment.getPatientName() == null){
            throw new ApiRequestException((
                    "patient name is mandatory"));
        }
        if(appointment.getDate() == null){
            throw new ApiRequestException((
                    "appointment date is mandatory"));
        }
        if(Objects.equals(appointment.getTime(), "") || appointment.getTime() == null){
            throw new ApiRequestException((
                    "appointment time is mandatory"));
        }
        if(appointment.getEmployeeId() == 0 ){
            throw new ApiRequestException((
                    "employee id is mandatory"));
        }
        if(Objects.equals(appointment.getEmployeeName(), "") || appointment.getEmployeeName() == null){
            throw new ApiRequestException((
                    "employee name is mandatory"));
        }
        if(Objects.equals(appointment.getReason(), "") || appointment.getReason()== null){
            throw new ApiRequestException((
                    "reason for appointment is mandatory"));
        }
        if(Objects.equals(appointment.getNotes(), "") || appointment.getNotes() == null){
            throw new ApiRequestException((
                    "notes is mandatory"));
        }
        if(Objects.equals(appointment.getStatus(), "") || appointment.getStatus() == null){
            throw new ApiRequestException((
                    "status is mandatory"));
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Appointment newAppointment, int appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiRequestException((
                        "Appointment with id " + appointmentId + " does not exists")));

        if (newAppointment.getNotes() != null &&
                !Objects.equals(appointment.getNotes(), newAppointment.getNotes())){
            appointment.setNotes(newAppointment.getNotes());
        }
        if (newAppointment.getStatus() != null &&
                !Objects.equals(appointment.getStatus(), newAppointment.getStatus())){
            appointment.setStatus(newAppointment.getStatus());
        }


        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment deleteAppointment(Appointment newAppointment, int appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiRequestException((
                        "Appointment with id " + appointmentId + " does not exists")));

        if (newAppointment.getNotes() != null &&
                !Objects.equals(appointment.getNotes(), newAppointment.getNotes())){
            appointment.setNotes(newAppointment.getNotes());
        }
        if (newAppointment.getStatus() != null &&
                !Objects.equals(appointment.getStatus(), newAppointment.getStatus())){
            appointment.setStatus(newAppointment.getStatus());
        }


        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getPatientAppointments(String email) {
        Optional<Integer> patientId  = Optional.of(00);
        if (patientRepository.findByEmail(email).isPresent()) {

            patientId = patientRepository.findByEmail(email).map(PatientRecord::getId);


        }
        return appointmentRepository.findAllByPatientId(patientId);


    }


}
