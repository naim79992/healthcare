package com.cmed.healthcare.repository;

import com.cmed.healthcare.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
List<Prescription> findByPrescriptionDateBetween(LocalDate start, LocalDate end);


@Query("SELECT p.prescriptionDate, COUNT(p) FROM Prescription p WHERE p.prescriptionDate BETWEEN :start AND :end GROUP BY p.prescriptionDate ORDER BY p.prescriptionDate")
List<Object[]> countDayWise(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
