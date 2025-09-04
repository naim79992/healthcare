package com.cmed.healthcare.controller;

import com.cmed.healthcare.model.Prescription;
import com.cmed.healthcare.model.User;
import com.cmed.healthcare.repository.PrescriptionRepository;
import com.cmed.healthcare.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository repo;
    private final UserRepository userRepo;

    public PrescriptionController(PrescriptionRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    // List prescriptions
    @GetMapping
    public List<Prescription> list(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            Authentication auth) {

        LocalDate s = (start == null) ? YearMonth.now().atDay(1) : LocalDate.parse(start);
        LocalDate e = (end == null) ? YearMonth.now().atEndOfMonth() : LocalDate.parse(end);

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        String role = currentUser.getRole();

        switch (role) {
            case "ADMIN":
            case "PHARMACIST":
            case "MEDICAL_STAFF":
                return repo.findByPrescriptionDateBetween(s, e);
            case "DOCTOR":
                return repo.findByPrescriptionDateBetweenAndDoctorName(s, e, currentUser.getUsername());
            case "USER":
                return repo.findByPrescriptionDateBetweenAndPatientName(s, e, currentUser.getUsername());
            default:
                return Collections.emptyList();
        }
    }

    // Get single prescription
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> get(@PathVariable Long id, Authentication auth) {
        Prescription presc = repo.findById(id).orElse(null);
        if (presc == null) return ResponseEntity.notFound().build();

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        String role = currentUser.getRole();

        if ((role.equals("USER") && !presc.getPatientName().equals(currentUser.getUsername())) ||
            (role.equals("DOCTOR") && !presc.getDoctorName().equals(currentUser.getUsername()))) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(presc);
    }

    // Create prescription (DOCTOR only)
    @PostMapping
    public ResponseEntity<Prescription> create(@Valid @RequestBody Prescription presc, Authentication auth) {
        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        if (!"DOCTOR".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).build();
        }

        // Keep patientName from form
        presc.setDoctorName(currentUser.getUsername());

        return ResponseEntity.ok(repo.save(presc));
    }

    // Update prescription (DOCTOR only)
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> update(@PathVariable Long id, @Valid @RequestBody Prescription presc, Authentication auth) {
        Prescription existing = repo.findById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        if (!currentUser.getRole().equals("DOCTOR") || !existing.getDoctorName().equals(currentUser.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        existing.setPrescriptionDate(presc.getPrescriptionDate());
        existing.setPatientName(presc.getPatientName());
        existing.setPatientAge(presc.getPatientAge());
        existing.setPatientGender(presc.getPatientGender());
        existing.setDiagnosis(presc.getDiagnosis());
        existing.setMedicines(presc.getMedicines());
        existing.setNextVisitDate(presc.getNextVisitDate());
        repo.save(existing);
        return ResponseEntity.ok(existing);
    }

    // Delete prescription (DOCTOR only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        Prescription existing = repo.findById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        if (!currentUser.getRole().equals("DOCTOR") || !existing.getDoctorName().equals(currentUser.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        repo.delete(existing);
        return ResponseEntity.noContent().build();
    }

    // Day-wise report
    @GetMapping("/report/daywise")
    public ResponseEntity<List<Map<String, Object>>> dayWiseReport(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            Authentication auth) {

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        String role = currentUser.getRole();

        LocalDate s = (start == null) ? YearMonth.now().atDay(1) : LocalDate.parse(start);
        LocalDate e = (end == null) ? YearMonth.now().atEndOfMonth() : LocalDate.parse(end);

        List<Object[]> rows;
        switch (role) {
    case "ADMIN":
    case "PHARMACIST":
    case "MEDICAL_STAFF":
        rows = repo.countDayWise(s, e);
        break;
    case "DOCTOR":
        rows = repo.countDayWiseForDoctor(s, e, currentUser.getUsername());
        break;
    case "USER":
        rows = repo.countDayWiseForPatient(s, e, currentUser.getUsername());
        break;
    default:
        return ResponseEntity.status(403).build();
}


        List<Map<String, Object>> report = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("day", r[0].toString());
            map.put("count", ((Number) r[1]).intValue());
            report.add(map);
        }
        return ResponseEntity.ok(report);
    }
}
