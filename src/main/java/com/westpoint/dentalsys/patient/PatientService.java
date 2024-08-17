package com.westpoint.dentalsys.patient;


import java.util.List;
import java.util.Optional;

public interface PatientService {
     PatientRecord savePatient(PatientRecord patient);

     List<PatientRecord> getAllPatients();

     PatientRecord updatePatient(PatientRecord patient, int patientId);

     PatientRecord deletePatient(PatientRecord patient, int patientId);

     Optional<PatientRecord>getPatient(String email);



}
