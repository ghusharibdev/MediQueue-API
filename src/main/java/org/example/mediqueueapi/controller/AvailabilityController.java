package org.example.mediqueueapi.controller;

import jakarta.validation.Valid;
import org.example.mediqueueapi.dto.AvailabilityRequest;
import org.example.mediqueueapi.dto.AvailabilityResponse;
import org.example.mediqueueapi.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<AvailabilityResponse> addAvailability(@PathVariable Long doctorId, @Valid @RequestBody AvailabilityRequest request) {
        return ResponseEntity.ok(availabilityService.addAvailability(doctorId, request));
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponse>> getAvailability(@PathVariable Long doctorId) {
        return ResponseEntity.ok(availabilityService.getAvailability(doctorId));
    }
}
