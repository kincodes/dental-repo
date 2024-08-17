package com.westpoint.dentalsys.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientRecord, Integer> {

    Optional<PatientRecord> findByEmail(String email);

    boolean existsByEmail(String email);
}
