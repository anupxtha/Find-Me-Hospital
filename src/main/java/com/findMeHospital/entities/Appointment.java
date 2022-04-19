package com.findMeHospital.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.findMeHospital.dto.AppointmentDto;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "appointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
    private Date requested_appointment_date;

    @DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
    private Date review_appointment_date;

    @Lob
    private String message;

    private int a_status;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Patient patient;
    
    private Boolean viewed;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private HospitalServiceDoctor hospitalServiceDoctor;

}
