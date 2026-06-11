package org.example.mediqueueapi.dto;

import org.example.mediqueueapi.model.Role;

public record AuthResponse(
        String token,
        Long id,
        String name,
        String email,
        Role role
) {
}
