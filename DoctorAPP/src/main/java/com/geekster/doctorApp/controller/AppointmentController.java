package com.geekster.doctorApp.controller;


import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("appointment")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentTime,
            @RequestParam String token) {

        Appointment appointment = appointmentService.createAppointment2(doctorId, patientId, appointmentTime,token);

        if (appointment != null) {
            return ResponseEntity.ok(appointment);
        } else {
            return ResponseEntity.badRequest().build();
        }


    }
}
