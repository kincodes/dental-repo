package com.westpoint.dentalsys.appointments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    //
  //  List<Appointment> findAllByPatientId(Integer patientId);

    List<Appointment> findAllByPatientId(Optional<Integer> patientId);
}
