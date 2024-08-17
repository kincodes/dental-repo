package com.westpoint.dentalsys.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/add")
    public String add(@RequestBody PatientRecord patient) {
        patientService.savePatient(patient);
        return "New patient added successfully!";
    }

    @GetMapping("/getAll")
    public List<PatientRecord> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/getPatient/{email}")
    public Optional<PatientRecord> getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PutMapping("update/{patientId}")
    public String updatePatient(@RequestBody PatientRecord patient, @PathVariable int patientId) {

        patientService.updatePatient(patient, patientId);

        return "Patient record updated successfully!";
    }

    @PutMapping("delete/{patientId}") //set patient inactive
    public String deletePatient(@RequestBody PatientRecord patient, @PathVariable int patientId) {

        patientService.deletePatient(patient, patientId);

        return "Patient record deleted!";
    }

}
