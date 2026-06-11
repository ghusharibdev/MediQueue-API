package org.example.mediqueueapi.controller;

import jakarta.validation.Valid;
import org.example.mediqueueapi.dto.CreateDoctorRequest;
import org.example.mediqueueapi.dto.DoctorResponse;
import org.example.mediqueueapi.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(doctorService.createDoctor(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctors(@RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(doctorService.getDoctors(specialization));
    }
}
