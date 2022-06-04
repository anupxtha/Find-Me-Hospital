package com.findMeHospital.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patient")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String p_name;

//    @Column(unique=true)
    private String p_email;

    private String p_password;

    private String p_gender;

    private Date date_of_birth;

//    @Column(unique=true)
    private String p_contact;

    private String address;

    @Lob
    private String p_info;

    private String p_photo;
    
    private String role;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    private City city;

}
