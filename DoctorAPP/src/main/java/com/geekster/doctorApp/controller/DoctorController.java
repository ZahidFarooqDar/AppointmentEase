package com.geekster.doctorApp.controller;
import com.geekster.doctorApp.ServiceModels.DoctorSM;
import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.model.Doctor;
import com.geekster.doctorApp.service.AuthenticationService;
import com.geekster.doctorApp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService docService;
    @Autowired
    AuthenticationService authService;

    @PostMapping()
    public Doctor addDoctors(@RequestBody DoctorSM doc)
    {
        return docService.addDoctor(doc);
    }

    @GetMapping("{docId}/appointments")
    ResponseEntity<List<Appointment>> getDocMyAppointments(@PathVariable Long docId)
    {

        List<Appointment> myAppointments = null;
        HttpStatus status;
        try
        {
            myAppointments = docService.getMyAppointments(docId);
            if(myAppointments.isEmpty())
            {
                status = HttpStatus.NO_CONTENT;
            }
            else
            {
                status = HttpStatus.OK;
            }
        }
        catch(Exception e)
        {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<List<Appointment>>(myAppointments,status);
    }
    @GetMapping()
    public ResponseEntity<List<Doctor>> getAllDoctors(@RequestParam String userEmail, @RequestParam String token)
    {
        HttpStatus status;
        List<Doctor> allDoctors = null;
        //token : calculate token -> find email in Db corr to this token-> match the emails
        if(authService.authenticate(userEmail,token))
        {
            allDoctors =  docService.getAllDoctors();
            status = HttpStatus.OK;
        }
        else
        {
            status = HttpStatus.FORBIDDEN;
        }
        ResponseEntity<List<Doctor>> responseEntity = new ResponseEntity<>(allDoctors, status);

        return responseEntity;
    }
}
