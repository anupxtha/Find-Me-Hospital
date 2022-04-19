package com.findMeHospital.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "doctor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    private int nmc_no;

    private String d_name;

    private String d_email;

    private String d_contact;

    private String d_gender;

    private Date date_of_birth;

    private String Specialty;

    private String Qualification;

    private int yearOfExperience;

    private String address;

    @Lob
    private String d_info;

    private String d_photo;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Room room;

}
