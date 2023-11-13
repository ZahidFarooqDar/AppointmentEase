package com.geekster.doctorApp.service;

import com.geekster.doctorApp.ServiceModels.DoctorSM;
import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.model.Doctor;
import com.geekster.doctorApp.repository.IDoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service
public class DoctorService {
    @Autowired
    IDoctorRepo doctorRepo;
    public Doctor addDoctor(@RequestBody DoctorSM doctor) {
        Doctor doc = new Doctor();
        doc.setDoctorId(doctor.getDoctorId());
        doc.setDoctorName(doctor.getDoctorName());
        doc.setSpecialization(doctor.getSpecialization());

        return doctorRepo.save(doc);
    }
    public List<Doctor> getAllDoctors() {
        List<Doctor> allDoctors = doctorRepo.findAll();
        return allDoctors;
    }
    public List<Appointment> getMyAppointments(Long docId) {
        Doctor myDoc = doctorRepo.findByDoctorId(docId);
        if(myDoc == null)
        {
            throw new IllegalStateException("The doctor does not exist");
        }
        return myDoc.getAppointments();
    }
}
