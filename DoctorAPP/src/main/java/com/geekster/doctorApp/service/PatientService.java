package com.geekster.doctorApp.service;
import com.geekster.doctorApp.dto.SignInInput;
import com.geekster.doctorApp.dto.SignInOutput;
import com.geekster.doctorApp.dto.SignUpInput;
import com.geekster.doctorApp.dto.SignUpOutput;
import com.geekster.doctorApp.model.*;
import com.geekster.doctorApp.repository.IAppointmentRepo;
import com.geekster.doctorApp.repository.IPatientRepo;
import com.geekster.doctorApp.repository.ITokenRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    IPatientRepo iPatientRepo;
    @Autowired
    AuthenticationService tokenService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    IPatientRepo patientRepo;
    @Autowired
    ITokenRepo tokenRepo;
    @Autowired
    IAppointmentRepo appointmentRepo;

    public SignUpOutput signUp(SignUpInput signUpDto) {

        //check if user exists or not based on email
        Patient patient = iPatientRepo.findFirstByPatientEmail(signUpDto.getUserEmail());

        if(patient != null)
        {
            throw new IllegalStateException("Patient already exists!!!!...sign in instead");
        }


        //encryption
        String encryptedPassword = null;
        try {
            encryptedPassword = encryptPassword(signUpDto.getUserPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        //save the user

        patient = new Patient(signUpDto.getUserFirstName(),
                signUpDto.getUserLastName(),signUpDto.getUserEmail(),
                encryptedPassword, signUpDto.getUserContact());

        iPatientRepo.save(patient);

        //token creation and saving

        AuthenticationToken token = new AuthenticationToken(patient);

        tokenService.saveToken(token);

        return new SignUpOutput("Patient registered","Patient created successfully");


    }

    private String encryptPassword(String userPassword) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(userPassword.getBytes());
        byte[] digested =  md5.digest();

        String hash = DatatypeConverter.printHexBinary(digested);
        return hash;
    }


    public SignInOutput signIn(SignInInput signInDto) {

        //get email

        Patient patient = iPatientRepo.findFirstByPatientEmail(signInDto.getPatientEmail());

        if(patient == null)
        {
            throw new IllegalStateException("User invalid!!!!...sign up instead");
        }

        //encrypt the password

        String encryptedPassword = null;

        try {
            encryptedPassword = encryptPassword(signInDto.getPatientPassword());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        //match it with database encrypted password

        boolean isPasswordValid = encryptedPassword.equals(patient.getPatientPassword());

        if(!isPasswordValid)
        {
            throw new IllegalStateException("User invalid!!!!...sign up instead");
        }

        //figure out the token

        AuthenticationToken authToken = tokenService.getToken(patient);

        //set up output response

        return new SignInOutput("Authentication Successfull !!!",authToken.getToken());


    }

    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
    public List<Appointment> getAllAppointments(Long patientId) {
        Patient patient = patientRepo.findByPatientId(patientId);
        //authenticatePatient(patient.getPatientId());
        return appointmentRepo.findByPatient_PatientId(patientId);
    }
    /*public List<Appointment> getAppointmentsForPatient(Long patientId, String Token) {
        //authenticatePatient(patientId,Token);
        //if(authenticatePatient(patientId,Token) == true) {

            List<Appointment> appointments = appointmentRepo.findByPatientId(patientId);
            return appointments;

        //throw new IllegalStateException("Unauthorized user");
    }*/

    public void cancelAppointment(AppointmentKey key) {

        appointmentService.cancelAppointment(key);

    }
    public Boolean authenticatePatient(Long patientId, String authToken) {
        // Fetch the patient by their ID
        Patient patient = patientRepo.findByPatientId(patientId);
        AuthenticationToken authenticationToken = tokenRepo.findByPatientAndToken(patient, authToken);

        if (patient == null || authenticationToken == null) {
            return false;
        }
        return  true;
    }
}
