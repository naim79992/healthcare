package com.cmed.healthcare.repository;

import com.cmed.healthcare.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // All prescriptions in date range
    List<Prescription> findByPrescriptionDateBetween(LocalDate start, LocalDate end);

    // For USER
    List<Prescription> findByPrescriptionDateBetweenAndPatientName(LocalDate start, LocalDate end, String patientName);

    // For DOCTOR
    List<Prescription> findByPrescriptionDateBetweenAndDoctorName(LocalDate start, LocalDate end, String doctorName);

    // Day-wise count report for all
    @Query("SELECT p.prescriptionDate, COUNT(p) FROM Prescription p " +
           "WHERE p.prescriptionDate BETWEEN :start AND :end " +
           "GROUP BY p.prescriptionDate " +
           "ORDER BY p.prescriptionDate")
    List<Object[]> countDayWise(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Day-wise count report for specific patient
    @Query("SELECT p.prescriptionDate, COUNT(p) FROM Prescription p " +
           "WHERE p.prescriptionDate BETWEEN :start AND :end " +
           "AND p.patientName = :patientName " +
           "GROUP BY p.prescriptionDate " +
           "ORDER BY p.prescriptionDate")
    List<Object[]> countDayWiseForPatient(@Param("start") LocalDate start,
                                          @Param("end") LocalDate end,
                                          @Param("patientName") String patientName);

    // Day-wise count report for specific doctor
    @Query("SELECT p.prescriptionDate, COUNT(p) FROM Prescription p " +
           "WHERE p.prescriptionDate BETWEEN :start AND :end " +
           "AND p.doctorName = :doctorName " +
           "GROUP BY p.prescriptionDate " +
           "ORDER BY p.prescriptionDate")
    List<Object[]> countDayWiseForDoctor(@Param("start") LocalDate start,
                                         @Param("end") LocalDate end,
                                         @Param("doctorName") String doctorName);
}
