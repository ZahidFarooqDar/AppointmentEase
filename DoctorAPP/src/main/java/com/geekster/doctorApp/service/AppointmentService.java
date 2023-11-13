package com.geekster.doctorApp.service;


import com.geekster.doctorApp.model.*;
import com.geekster.doctorApp.repository.IAppointmentRepo;
import com.geekster.doctorApp.repository.IDoctorRepo;
import com.geekster.doctorApp.repository.IPatientRepo;
import com.geekster.doctorApp.repository.ITokenRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    IAppointmentRepo appointmentRepo;
    @Autowired
    IDoctorRepo doctorRepository;
    @Autowired
    IPatientRepo patientRepository;
    @Autowired
    ITokenRepo authenticationTokenRepo;
    public Appointment createAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime, String authToken) {
        Patient patient = patientRepository.findByPatientId(patientId);
        AuthenticationToken authenticationToken = authenticationTokenRepo.findByPatientAndToken(patient, authToken);

        if (patient == null || authenticationToken == null) {
            throw new EntityNotFoundException("Patient not found or invalid token.");
        }
        Doctor doctor = doctorRepository.findByDoctorId(doctorId);
        if (doctor == null ) {
            throw new EntityNotFoundException("Doctor not found.");
        }
            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setAppointmentDate(appointmentTime);
            Appointment savedAppointment = appointmentRepo.save(appointment);

            return savedAppointment;
    }
    @Transactional
    public void cancelAppointment(Long appointmentId,Long patientId) {
        appointmentRepo.deleteByIdAndPatientId(appointmentId, patientId);
    }
    public boolean isAppointmentTimeAvailable(Long doctorId, LocalDateTime appointmentTime) {

        Doctor doctor = doctorRepository.findByDoctorId(doctorId);
        List<Appointment> existingAppointments = appointmentRepo.findByDoctorAndAppointmentDate(doctor, appointmentTime);
        return existingAppointments.isEmpty();
    }
}
