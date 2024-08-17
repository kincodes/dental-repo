package com.westpoint.dentalsys.patient;

import com.westpoint.dentalsys.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public PatientRecord savePatient(PatientRecord patient) {

        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new ApiRequestException((
                    "patient with email " + patient.getEmail() + " already exists"));
        }

        if (Objects.equals(patient.getFirstName(), "") || patient.getFirstName() == null) {
            throw new ApiRequestException((
                    "firstname is mandatory"));

        }
        if (patient.getDob() == null) {
            throw new ApiRequestException((
                    "date of birth is mandatory"));

        }
        if (Objects.equals(patient.getEmail(), "") || patient.getEmail() == null) {
            throw new ApiRequestException((
                    "email is mandatory"));

        }
        if (Objects.equals(patient.getPhone(), "") || patient.getPhone() == null) {
            throw new ApiRequestException((
                    "phone number is mandatory"));

        }
        if (Objects.equals(patient.getStreet(), "") || patient.getStreet() == null) {
            throw new ApiRequestException((
                    "street is mandatory"));

        }
        if (Objects.equals(patient.getCity(), "") || patient.getCity() == null) {
            throw new ApiRequestException((
                    "city is mandatory"));

        }
        if (Objects.equals(patient.getProvince(), "") || patient.getProvince() == null) {
            throw new ApiRequestException((
                    "province is mandatory"));

        }
        if (Objects.equals(patient.getPostal(), "") || patient.getPostal() == null) {
            throw new ApiRequestException((
                    "postal code is mandatory"));

        }
        if (Objects.equals(patient.getGender(), "") || patient.getGender() == null) {
            throw new ApiRequestException((
                    "gender is mandatory"));

        }
        if (Objects.equals(patient.getInsuranceProvider(), "") || patient.getInsuranceProvider() == null) {
            throw new ApiRequestException((
                    "insurance provider is mandatory"));

        }
        if (Objects.equals(patient.getPolicyNumber(), "") || patient.getPolicyNumber() == null) {
            throw new ApiRequestException((
                    "policy number is mandatory"));

        }
        if (Objects.equals(patient.getNextOfKin(), "") || patient.getNextOfKin() == null) {
            throw new ApiRequestException((
                    "next of kin is mandatory"));

        }

        if (Objects.equals(patient.getKinPhone(), "") || patient.getKinPhone() == null) {
            throw new ApiRequestException((
                    "kin's phone number is mandatory"));

        }

        if (Objects.equals(patient.getKinRelationship(), "") || patient.getKinRelationship() == null) {
            throw new ApiRequestException((
                    "relationship of is mandatory"));

        }

        if (Objects.equals(patient.getStatus(), "") || patient.getStatus() == null) {
            throw new ApiRequestException((
                    "status is mandatory"));

        }


        return patientRepository.save(patient);

    }

    @Override
    public List<PatientRecord> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public PatientRecord updatePatient(PatientRecord patientUpdate, int patientId) {

        PatientRecord patientRecord = patientRepository.findById(patientId)
                .orElseThrow(() -> new ApiRequestException((
                        "patient with id " + patientId + " does not exists")));

        if (patientUpdate.getFirstName() != null &&
                !Objects.equals(patientRecord.getFirstName(), patientUpdate.getFirstName())) {
            patientRecord.setFirstName(patientUpdate.getFirstName());
        }
        if (patientUpdate.getLastName() != null &&
                !Objects.equals(patientRecord.getLastName(), patientUpdate.getLastName())) {
            patientRecord.setLastName(patientUpdate.getLastName());
        }
        if (patientUpdate.getDob() != null &&
                !Objects.equals(patientRecord.getDob(), patientUpdate.getDob())) {
            patientRecord.setDob(patientUpdate.getDob());
        }
        if (patientUpdate.getEmail() != null &&
                !Objects.equals(patientRecord.getEmail(), patientUpdate.getEmail())) {
            patientRecord.setEmail(patientUpdate.getEmail());
        }
        if (patientUpdate.getPhone() != null &&
                !Objects.equals(patientRecord.getPhone(), patientUpdate.getPhone())) {
            patientRecord.setPhone(patientUpdate.getPhone());
        }
        if (patientUpdate.getGender() != null &&
                !Objects.equals(patientRecord.getGender(), patientUpdate.getGender())) {
            patientRecord.setGender(patientUpdate.getGender());
        }
        if (patientUpdate.getStreet() != null &&
                !Objects.equals(patientRecord.getStreet(), patientUpdate.getStreet())) {
            patientRecord.setStreet(patientUpdate.getStreet());
        }
        if (patientUpdate.getCity() != null &&
                !Objects.equals(patientRecord.getCity(), patientUpdate.getCity())) {
            patientRecord.setCity(patientUpdate.getCity());
        }
        if (patientUpdate.getProvince() != null &&
                !Objects.equals(patientRecord.getProvince(), patientUpdate.getProvince())) {
            patientRecord.setProvince(patientUpdate.getProvince());
        }
        if (patientUpdate.getPostal() != null &&
                !Objects.equals(patientRecord.getPostal(), patientUpdate.getPostal())) {
            patientRecord.setPostal(patientUpdate.getPostal());
        }
        if (patientUpdate.getInsuranceProvider() != null &&
                !Objects.equals(patientRecord.getInsuranceProvider(), patientUpdate.getInsuranceProvider())) {
            patientRecord.setInsuranceProvider(patientUpdate.getInsuranceProvider());
        }
        if (patientUpdate.getPolicyNumber() != null &&
                !Objects.equals(patientRecord.getPolicyNumber(), patientUpdate.getPolicyNumber())) {
            patientRecord.setPolicyNumber(patientUpdate.getPolicyNumber());
        }

        if (patientUpdate.getNextOfKin() != null &&
                !Objects.equals(patientRecord.getNextOfKin(), patientUpdate.getNextOfKin())) {
            patientRecord.setNextOfKin(patientUpdate.getNextOfKin());
        }
        if (patientUpdate.getKinPhone() != null &&
                !Objects.equals(patientRecord.getKinPhone(), patientUpdate.getKinPhone())) {
            patientRecord.setKinPhone(patientUpdate.getKinPhone());
        }
        if (patientUpdate.getKinRelationship() != null &&
                !Objects.equals(patientRecord.getKinRelationship(), patientUpdate.getKinRelationship())) {
            patientRecord.setKinRelationship(patientUpdate.getKinRelationship());
        }


        return patientRepository.save(patientRecord);
    }

    @Override
    public PatientRecord deletePatient(PatientRecord patientUpdate, int patientId) {

        PatientRecord patientRecord = patientRepository.findById(patientId)
                .orElseThrow(() -> new ApiRequestException((
                        "patient with id " + patientId + " does not exists")));

        if (patientUpdate.getFirstName() != null &&
                !Objects.equals(patientRecord.getFirstName(), patientUpdate.getFirstName())) {
            patientRecord.setFirstName(patientUpdate.getFirstName());
        }
        if (patientUpdate.getLastName() != null &&
                !Objects.equals(patientRecord.getLastName(), patientUpdate.getLastName())) {
            patientRecord.setLastName(patientUpdate.getLastName());
        }
        if (patientUpdate.getDob() != null &&
                !Objects.equals(patientRecord.getDob(), patientUpdate.getDob())) {
            patientRecord.setDob(patientUpdate.getDob());
        }
        if (patientUpdate.getEmail() != null &&
                !Objects.equals(patientRecord.getEmail(), patientUpdate.getEmail())) {
            patientRecord.setEmail(patientUpdate.getEmail());
        }
        if (patientUpdate.getPhone() != null &&
                !Objects.equals(patientRecord.getPhone(), patientUpdate.getPhone())) {
            patientRecord.setPhone(patientUpdate.getPhone());
        }
        if (patientUpdate.getGender() != null &&
                !Objects.equals(patientRecord.getGender(), patientUpdate.getGender())) {
            patientRecord.setGender(patientUpdate.getGender());
        }
        if (patientUpdate.getStreet() != null &&
                !Objects.equals(patientRecord.getStreet(), patientUpdate.getStreet())) {
            patientRecord.setStreet(patientUpdate.getStreet());
        }
        if (patientUpdate.getCity() != null &&
                !Objects.equals(patientRecord.getCity(), patientUpdate.getCity())) {
            patientRecord.setCity(patientUpdate.getCity());
        }
        if (patientUpdate.getProvince() != null &&
                !Objects.equals(patientRecord.getProvince(), patientUpdate.getProvince())) {
            patientRecord.setProvince(patientUpdate.getProvince());
        }
        if (patientUpdate.getPostal() != null &&
                !Objects.equals(patientRecord.getPostal(), patientUpdate.getPostal())) {
            patientRecord.setPostal(patientUpdate.getPostal());
        }
        if (patientUpdate.getInsuranceProvider() != null &&
                !Objects.equals(patientRecord.getInsuranceProvider(), patientUpdate.getInsuranceProvider())) {
            patientRecord.setInsuranceProvider(patientUpdate.getInsuranceProvider());
        }
        if (patientUpdate.getPolicyNumber() != null &&
                !Objects.equals(patientRecord.getPolicyNumber(), patientUpdate.getPolicyNumber())) {
            patientRecord.setPolicyNumber(patientUpdate.getPolicyNumber());
        }

        if (patientUpdate.getNextOfKin() != null &&
                !Objects.equals(patientRecord.getNextOfKin(), patientUpdate.getNextOfKin())) {
            patientRecord.setNextOfKin(patientUpdate.getNextOfKin());
        }
        if (patientUpdate.getKinPhone() != null &&
                !Objects.equals(patientRecord.getKinPhone(), patientUpdate.getKinPhone())) {
            patientRecord.setKinPhone(patientUpdate.getKinPhone());
        }
        if (patientUpdate.getKinRelationship() != null &&
                !Objects.equals(patientRecord.getKinRelationship(), patientUpdate.getKinRelationship())) {
            patientRecord.setKinRelationship(patientUpdate.getKinRelationship());
        }
        if (patientUpdate.getStatus() != null &&
                !Objects.equals(patientRecord.getStatus(), patientUpdate.getStatus())) {
            patientRecord.setStatus(patientUpdate.getStatus());
        }


        return patientRepository.save(patientRecord);
    }

    @Override
    public Optional<PatientRecord> getPatient(String email) {


        PatientRecord patientRecord = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException((
                        "patient with email " + email + " does not exists")));

        return patientRepository.findByEmail(email);
    }


}
