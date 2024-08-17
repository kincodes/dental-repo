package com.westpoint.dentalsys.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
public class PatientRecord {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotNull(message = "Date of Birth is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dob;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @NotBlank(message = "Street is mandatory")
    private String street;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Province is mandatory")
    private String province;

    @NotBlank(message = "Postal code is mandatory")
    private String postal;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotBlank(message = "Insurance Provider is mandatory")
    private String insuranceProvider;

    @NotBlank(message = "Policy Number is mandatory")
    private String policyNumber;

    @NotBlank(message = "Name of Kin is mandatory")
    private String nextOfKin;

    @NotBlank(message = "Kin's phone number is mandatory")
    private String kinPhone;

    @NotBlank(message = "Relationship of kin is mandatory")
    private String kinRelationship;

    @NotNull(message = "Balance is mandatory")
    private double balance = 0.0;

    @NotBlank(message = "Status is mandatory")
    private String status;


}
