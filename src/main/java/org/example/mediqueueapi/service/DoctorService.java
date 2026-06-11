package org.example.mediqueueapi.service;

import org.example.mediqueueapi.dto.CreateDoctorRequest;
import org.example.mediqueueapi.dto.DoctorResponse;
import org.example.mediqueueapi.exception.ApiException;
import org.example.mediqueueapi.model.Doctor;
import org.example.mediqueueapi.model.Role;
import org.example.mediqueueapi.model.User;
import org.example.mediqueueapi.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;

    public DoctorService(DoctorRepository doctorRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
    }

    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        User user = userService.getById(request.userId());
        if (user.getRole() != Role.DOCTOR) {
            throw new ApiException("User role must be DOCTOR");
        }
        if (doctorRepository.existsByUser(user)) {
            throw new ApiException("Doctor profile already exists for this user");
        }
        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.specialization())
                .consultationFee(request.consultationFee())
                .build();
        return toResponse(doctorRepository.save(doctor));
    }

    public List<DoctorResponse> getDoctors(String specialization) {
        List<Doctor> doctors = specialization == null || specialization.isBlank()
                ? doctorRepository.findAll()
                : doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
        return doctors.stream().map(this::toResponse).toList();
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ApiException("Doctor not found"));
    }

    public DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getId(),
                doctor.getUser().getName(),
                doctor.getUser().getEmail(),
                doctor.getSpecialization(),
                doctor.getConsultationFee()
        );
    }
}
