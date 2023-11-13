package com.geekster.doctorApp.controller;


import com.geekster.doctorApp.dto.SignInInput;
import com.geekster.doctorApp.dto.SignInOutput;
import com.geekster.doctorApp.dto.SignUpInput;
import com.geekster.doctorApp.dto.SignUpOutput;
import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.model.AppointmentKey;
import com.geekster.doctorApp.model.Doctor;
import com.geekster.doctorApp.model.Patient;
import com.geekster.doctorApp.service.AuthenticationService;
import com.geekster.doctorApp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    PatientService patientService;
    @Autowired
    AuthenticationService authService;
    @PostMapping("/signup")
    public SignUpOutput signup(@RequestBody SignUpInput signUpDto)
    {
        return patientService.signUp(signUpDto);
    }
    @PostMapping("/signin")
    public SignInOutput signup(@RequestBody SignInInput signInDto)
    {
        return patientService.signIn(signInDto);
    }

    @DeleteMapping("appointment")
    ResponseEntity<Void> cancelAppointment(@RequestParam String userEmail, @RequestParam String token,
                                           @RequestParam Long patientId,
                                           @RequestParam Long appointmentId)
    {
        HttpStatus status;
        if(authService.authenticate(userEmail,token))
        {
            patientService.cancelAppointment(appointmentId,patientId);
            status = HttpStatus.OK;
        }
        else
        {
            status = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<Void>(status);
    }
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(@RequestParam Long PatientId, @RequestParam String userEmail, @RequestParam String token)
    {
        HttpStatus status;
        List<Appointment> allAppointments = null;
        long pId;
        if(authService.authenticate(userEmail,token))
        {
            Long Pid = PatientId;
            allAppointments =  patientService.getAllAppointments(Pid);
            status = HttpStatus.OK;
        }
        else
        {
            status = HttpStatus.FORBIDDEN;
        }
        ResponseEntity<List<Appointment>> responseEntity = new ResponseEntity<>(allAppointments, status);
        return responseEntity;
    }
}
