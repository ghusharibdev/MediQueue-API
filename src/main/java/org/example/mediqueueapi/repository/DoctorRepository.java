package org.example.mediqueueapi.repository;

import org.example.mediqueueapi.model.Doctor;
import org.example.mediqueueapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
    boolean existsByUser(User user);
}
