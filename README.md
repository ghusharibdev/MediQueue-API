# MediQueue API

Simple clinic appointment and queue management REST API built with Java 25, Spring Boot, Spring Security, JWT resource server, Spring Data JPA, Validation, Lombok, and PostgreSQL.

## Setup

Create database:

```sql
CREATE DATABASE mediqueue_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.username=postgres
spring.datasource.password=your_password
```

Run:

```bash
mvn spring-boot:run
```

## Auth Flow

Register a user, login, copy token, then use:

```http
Authorization: Bearer YOUR_TOKEN
```

## Roles

```text
ADMIN
DOCTOR
RECEPTIONIST
PATIENT
```

## Main Endpoints

```http
POST /api/auth/register
POST /api/auth/login
POST /api/doctors
GET /api/doctors
POST /api/doctors/{doctorId}/availability
GET /api/doctors/{doctorId}/availability
POST /api/appointments
GET /api/appointments
GET /api/appointments/queue?doctorId=1&date=2026-06-20
PATCH /api/appointments/{id}/cancel
PATCH /api/appointments/{id}/check-in
PATCH /api/appointments/{id}/complete
PATCH /api/appointments/{id}/no-show
```

## Sample Requests

### Register Patient

```json
{
  "name": "Ali Patient",
  "email": "patient@example.com",
  "password": "123456",
  "role": "PATIENT"
}
```

### Register Doctor User

```json
{
  "name": "Dr Ahmed",
  "email": "doctor@example.com",
  "password": "123456",
  "role": "DOCTOR"
}
```

### Create Doctor Profile

```json
{
  "userId": 2,
  "specialization": "Cardiology",
  "consultationFee": 2500
}
```

### Add Availability

```json
{
  "dayOfWeek": "MONDAY",
  "startTime": "10:00:00",
  "endTime": "15:00:00"
}
```

### Book Appointment

```json
{
  "doctorId": 1,
  "appointmentDate": "2026-06-22",
  "appointmentTime": "11:00:00",
  "reason": "Chest pain checkup"
}
```

## Resume Bullets

```text
• Built a clinic appointment and queue management REST API with doctor profiles, availability slots, patient booking, queue generation, appointment status tracking, JWT authentication, and PostgreSQL persistence.
• Implemented booking rules to prevent duplicate time slots, reject appointments outside doctor availability, generate daily queue numbers, and support cancellation/check-in/completion workflows.
• Used MVC architecture with DTO validation, Spring Security, Spring Data JPA relationships, centralized exception handling, and transactional service methods for consistent appointment data.
```
