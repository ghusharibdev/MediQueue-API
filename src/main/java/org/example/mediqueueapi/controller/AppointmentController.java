package org.example.mediqueueapi.controller;

import jakarta.validation.Valid;
import org.example.mediqueueapi.dto.AppointmentResponse;
import org.example.mediqueueapi.dto.BookAppointmentRequest;
import org.example.mediqueueapi.model.AppointmentStatus;
import org.example.mediqueueapi.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody BookAppointmentRequest request, Authentication authentication) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request, authentication));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status
    ) {
        return ResponseEntity.ok(appointmentService.getAppointments(doctorId, patientId, date, status));
    }

    @GetMapping("/queue")
    public ResponseEntity<List<AppointmentResponse>> getQueue(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(appointmentService.getQueue(doctorId, date));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, authentication));
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<AppointmentResponse> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, AppointmentStatus.CHECKED_IN));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, AppointmentStatus.COMPLETED));
    }

    @PatchMapping("/{id}/no-show")
    public ResponseEntity<AppointmentResponse> noShow(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, AppointmentStatus.NO_SHOW));
    }
}
