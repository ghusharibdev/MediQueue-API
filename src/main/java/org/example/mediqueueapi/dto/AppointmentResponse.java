package org.example.mediqueueapi.dto;

import org.example.mediqueueapi.model.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        Long doctorId,
        String doctorName,
        Long patientId,
        String patientName,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        Integer queueNumber,
        AppointmentStatus status,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime cancelledAt
) {
}
