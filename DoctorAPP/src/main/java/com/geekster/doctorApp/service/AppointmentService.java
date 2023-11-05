package com.geekster.doctorApp.service;


import com.geekster.doctorApp.model.*;
import com.geekster.doctorApp.repository.IAppointmentRepo;
import com.geekster.doctorApp.repository.IDoctorRepo;
import com.geekster.doctorApp.repository.IPatientRepo;
import com.geekster.doctorApp.repository.ITokenRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        AuthenticationToken authenticationToken = authenticationTokenRepo.findFirstByToken(authToken);
        /*
        if (authenticationToken == null || !authenticationToken.isValid()) {
            throw new InvalidTokenException("Invalid or expired authentication token.");
        }*/
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        Patient patient = patientRepository.findByPatientId(patientId);

        if (doctor == null || patient == null) {
            throw new EntityNotFoundException("Doctor or patient not found.");
        }

        // Create a new appointment with a valid ID
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentTime);

        // Save the appointment
        Appointment savedAppointment = appointmentRepo.save(appointment);

        return savedAppointment;
    }
    public Appointment createAppointment2(Long doctorId, Long patientId, LocalDateTime appointmentTime, String authToken) {
        Patient patient = patientRepository.findByPatientId(patientId);
        AuthenticationToken authenticationToken = authenticationTokenRepo.findByPatientAndToken(patient, authToken);

        if (patient == null || authenticationToken == null) {
            throw new EntityNotFoundException("Patient not found or invalid token.");
        }
        Doctor doctor = doctorRepository.findByDoctorId(doctorId);
        //Patient patient = patientRepository.findByPatientId(patientId);

        if (doctor == null ) {
            throw new EntityNotFoundException("Doctor not found.");
        }

        // Create a new appointment with a valid ID
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentTime);

        // Save the appointment
        Appointment savedAppointment = appointmentRepo.save(appointment);

        return savedAppointment;
    }


    public void cancelAppointment(AppointmentKey key) {
        appointmentRepo.deleteById(key);
    }
}
