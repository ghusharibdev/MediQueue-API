package org.example.mediqueueapi.service;

import org.example.mediqueueapi.dto.AvailabilityRequest;
import org.example.mediqueueapi.dto.AvailabilityResponse;
import org.example.mediqueueapi.exception.ApiException;
import org.example.mediqueueapi.model.Doctor;
import org.example.mediqueueapi.model.DoctorAvailability;
import org.example.mediqueueapi.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvailabilityService {
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorService doctorService;

    public AvailabilityService(DoctorAvailabilityRepository availabilityRepository, DoctorService doctorService) {
        this.availabilityRepository = availabilityRepository;
        this.doctorService = doctorService;
    }

    public AvailabilityResponse addAvailability(Long doctorId, AvailabilityRequest request) {
        if (!request.startTime().isBefore(request.endTime())) {
            throw new ApiException("Start time must be before end time");
        }
        Doctor doctor = doctorService.getDoctor(doctorId);
        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();
        return toResponse(availabilityRepository.save(availability));
    }

    public List<AvailabilityResponse> getAvailability(Long doctorId) {
        return availabilityRepository.findByDoctorId(doctorId).stream().map(this::toResponse).toList();
    }

    public boolean isDoctorAvailable(Doctor doctor, java.time.LocalDate date, java.time.LocalTime time) {
        return availabilityRepository.findByDoctorAndDayOfWeek(doctor, date.getDayOfWeek()).stream()
                .anyMatch(a -> !time.isBefore(a.getStartTime()) && time.isBefore(a.getEndTime()));
    }

    private AvailabilityResponse toResponse(DoctorAvailability availability) {
        return new AvailabilityResponse(
                availability.getId(),
                availability.getDoctor().getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        );
    }
}
