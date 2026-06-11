package org.example.mediqueueapi.dto;

public record DoctorResponse(
        Long id,
        Long userId,
        String name,
        String email,
        String specialization,
        Double consultationFee
) {
}
