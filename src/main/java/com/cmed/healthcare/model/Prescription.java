package com.cmed.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;


@Entity
public class Prescription {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@NotNull(message = "Prescription date is required")
private LocalDate prescriptionDate;


@NotBlank(message = "Patient name is required")
private String patientName;


@NotNull(message = "Patient age is required")
@Min(value = 0, message = "Age must be >= 0")
@Max(value = 150, message = "Age must be <= 150")
private Integer patientAge;


@NotBlank(message = "Patient gender is required")
private String patientGender; // e.g. Male/Female/Other


@Lob
private String diagnosis;


@Lob
private String medicines;


private LocalDate nextVisitDate;


public Long getId() {
    return id;
}


public void setId(Long id) {
    this.id = id;
}


public LocalDate getPrescriptionDate() {
    return prescriptionDate;
}


public void setPrescriptionDate(LocalDate prescriptionDate) {
    this.prescriptionDate = prescriptionDate;
}


public String getPatientName() {
    return patientName;
}


public void setPatientName(String patientName) {
    this.patientName = patientName;
}


public Integer getPatientAge() {
    return patientAge;
}


public void setPatientAge(Integer patientAge) {
    this.patientAge = patientAge;
}


public String getPatientGender() {
    return patientGender;
}


public void setPatientGender(String patientGender) {
    this.patientGender = patientGender;
}


public String getDiagnosis() {
    return diagnosis;
}


public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
}


public String getMedicines() {
    return medicines;
}


public void setMedicines(String medicines) {
    this.medicines = medicines;
}


public LocalDate getNextVisitDate() {
    return nextVisitDate;
}


public void setNextVisitDate(LocalDate nextVisitDate) {
    this.nextVisitDate = nextVisitDate;
}


}