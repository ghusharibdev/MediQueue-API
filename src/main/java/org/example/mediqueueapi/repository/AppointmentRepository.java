package org.example.mediqueueapi.repository;

import org.example.mediqueueapi.model.Appointment;
import org.example.mediqueueapi.model.AppointmentStatus;
import org.example.mediqueueapi.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndAppointmentDateOrderByQueueNumberAsc(Long doctorId, LocalDate date);
    List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(Long patientId);
    List<Appointment> findByStatus(AppointmentStatus status);
    boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(Doctor doctor, LocalDate date, LocalTime time, AppointmentStatus status);
    long countByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDate date, AppointmentStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Appointment a where a.doctor = :doctor and a.appointmentDate = :date")
    List<Appointment> findDoctorDayAppointmentsForUpdate(@Param("doctor") Doctor doctor, @Param("date") LocalDate date);
}
