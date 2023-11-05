package com.geekster.doctorApp.ServiceModels;

import com.geekster.doctorApp.model.Appointment;
import com.geekster.doctorApp.model.Specialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSM
{

    private Long doctorId;
    private String doctorName;
    private Specialization specialization;
}
