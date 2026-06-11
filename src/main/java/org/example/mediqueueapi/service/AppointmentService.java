package org.example.mediqueueapi.service;

import org.example.mediqueueapi.dto.AppointmentResponse;
import org.example.mediqueueapi.dto.BookAppointmentRequest;
import org.example.mediqueueapi.exception.ApiException;
import org.example.mediqueueapi.model.*;
import org.example.mediqueueapi.repository.AppointmentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final UserService userService;
    private final AvailabilityService availabilityService;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService, UserService userService, AvailabilityService availabilityService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.userService = userService;
        this.availabilityService = availabilityService;
    }

    @Transactional
    public AppointmentResponse bookAppointment(BookAppointmentRequest request, Authentication authentication) {
        User patient = userService.getCurrentUser(authentication);
        if (patient.getRole() != Role.PATIENT) {
            throw new ApiException("Only patients can book appointments");
        }
        Doctor doctor = doctorService.getDoctor(request.doctorId());
        if (request.appointmentDate().isBefore(LocalDate.now())) {
            throw new ApiException("Appointment date cannot be in the past");
        }
        if (!availabilityService.isDoctorAvailable(doctor, request.appointmentDate(), request.appointmentTime())) {
            throw new ApiException("Doctor is not available at this time");
        }
        appointmentRepository.findDoctorDayAppointmentsForUpdate(doctor, request.appointmentDate());
        if (appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(doctor, request.appointmentDate(), request.appointmentTime(), AppointmentStatus.CANCELLED)) {
            throw new ApiException("This time slot is already booked");
        }
        long queueCount = appointmentRepository.countByDoctorAndAppointmentDateAndStatusNot(doctor, request.appointmentDate(), AppointmentStatus.CANCELLED);
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .queueNumber((int) queueCount + 1)
                .status(AppointmentStatus.BOOKED)
                .reason(request.reason())
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAppointments(Long doctorId, Long patientId, LocalDate date, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(a -> doctorId == null || a.getDoctor().getId().equals(doctorId))
                .filter(a -> patientId == null || a.getPatient().getId().equals(patientId))
                .filter(a -> date == null || a.getAppointmentDate().equals(date))
                .filter(a -> status == null || a.getStatus() == status)
                .sorted(Comparator.comparing(Appointment::getAppointmentDate).thenComparing(Appointment::getAppointmentTime))
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> getQueue(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateOrderByQueueNumberAsc(doctorId, date).stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AppointmentResponse cancelAppointment(Long id, Authentication authentication) {
        Appointment appointment = getAppointment(id);
        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser.getRole() == Role.PATIENT && !appointment.getPatient().getId().equals(currentUser.getId())) {
            throw new ApiException("You can cancel only your own appointments");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ApiException("Appointment is already cancelled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ApiException("Completed appointment cannot be cancelled");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledAt(LocalDateTime.now());
        return toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = getAppointment(id);
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ApiException("Cancelled appointment status cannot be changed");
        }
        appointment.setStatus(status);
        return toResponse(appointmentRepository.save(appointment));
    }

    private Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Appointment not found"));
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getUser().getName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getQueueNumber(),
                appointment.getStatus(),
                appointment.getReason(),
                appointment.getCreatedAt(),
                appointment.getCancelledAt()
        );
    }
}
