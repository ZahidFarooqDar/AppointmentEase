package com.geekster.doctorApp.repository;

import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.model.AppointmentKey;
import com.geekster.doctorApp.model.Doctor;
import com.geekster.doctorApp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentRepo extends JpaRepository<Appointment, AppointmentKey> {
    Appointment findById(Long id);
    List<Appointment> findByPatient_PatientId(Long patientId);
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.id = :appointmentId AND a.patient.id = :patientId")
    void deleteByIdAndPatientId(@Param("appointmentId") Long appointmentId, @Param("patientId") Long patientId);
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentTime);
}
