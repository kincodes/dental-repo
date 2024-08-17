package com.westpoint.dentalsys.appointments;

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
public class Appointment {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Patient ID is mandatory")
    private int patientId;

    @NotBlank(message = "Patient name is mandatory")
    private String patientName;

    @NotNull(message = "Appointment date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @NotBlank(message = "Time is mandatory")
    private String time;

    @NotNull(message = "Employee Id is mandatory")
    private int employeeId;

    @NotBlank(message = "Employee name is mandatory")
    private String employeeName;

    @NotBlank(message = "Reason for visit is mandatory")
    private String reason;

    @NotBlank(message = "Notes is mandatory")
    private String notes;

    @NotBlank(message = "Status is mandatory")
    private String status;







}
