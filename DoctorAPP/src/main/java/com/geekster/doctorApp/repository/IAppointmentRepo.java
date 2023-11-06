package com.geekster.doctorApp.repository;

import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.model.AppointmentKey;
import com.geekster.doctorApp.model.Doctor;
import com.geekster.doctorApp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentRepo extends JpaRepository<Appointment, AppointmentKey> {

    //public String findByIdAppId(Long id);
    Appointment findById(Long id);
    List<Appointment> findByPatient_PatientId(Long patientId);

    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentTime);

    /*List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatient_PatientId(Long patientId);*/

}
