package org.example.mediqueueapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateDoctorRequest(
        @NotNull Long userId,
        @NotBlank String specialization,
        @Positive Double consultationFee
) {
}
