package com.cmed.healthcare.controller;

import com.cmed.healthcare.model.Prescription;
import com.cmed.healthcare.repository.PrescriptionRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository repo;

    public PrescriptionController(PrescriptionRepository repo) {
        this.repo = repo;
    }

    // Get prescriptions list (optional date range)
    @GetMapping
    public List<Prescription> list(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {

        LocalDate s = (start == null) ? YearMonth.now().atDay(1) : LocalDate.parse(start);
        LocalDate e = (end == null) ? YearMonth.now().atEndOfMonth() : LocalDate.parse(end);

        return repo.findByPrescriptionDateBetween(s, e);
    }

    // Get single prescription
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Create new prescription
    @PostMapping
    public ResponseEntity<Prescription> create(@Valid @RequestBody Prescription presc) {
        return ResponseEntity.ok(repo.save(presc));
    }

    // Update prescription
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> update(@PathVariable Long id, @Valid @RequestBody Prescription presc) {
        return repo.findById(id).map(existing -> {
            existing.setPrescriptionDate(presc.getPrescriptionDate());
            existing.setPatientName(presc.getPatientName());
            existing.setPatientAge(presc.getPatientAge());
            existing.setPatientGender(presc.getPatientGender());
            existing.setDiagnosis(presc.getDiagnosis());
            existing.setMedicines(presc.getMedicines());
            existing.setNextVisitDate(presc.getNextVisitDate());
            repo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete prescription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Day-wise prescription count report
    @GetMapping("/report/daywise")
    public List<Map<String, Object>> dayWiseReport(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {

        LocalDate s = (start == null) ? YearMonth.now().atDay(1) : LocalDate.parse(start);
        LocalDate e = (end == null) ? YearMonth.now().atEndOfMonth() : LocalDate.parse(end);

        // Make sure repository has this method:
        // @Query("SELECT p.prescriptionDate, COUNT(p) FROM Prescription p WHERE p.prescriptionDate BETWEEN :start AND :end GROUP BY p.prescriptionDate")
        List<Object[]> rows = repo.countDayWise(s, e);

        return rows.stream().map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("day", r[0].toString());
            m.put("count", ((Number) r[1]).intValue());
            return m;
        }).collect(Collectors.toList());
    }

    // Get all prescriptions (REST API)
@GetMapping("/all")
public List<Prescription> getAllPrescriptions() {
    return repo.findAll();
}

}
