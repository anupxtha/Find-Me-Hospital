package com.findMeHospital.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hospital")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String h_name;

    private String h_email;

    private String h_password;

    private String h_contact;

    private String emergency_contact;

    private Date established;

    private String certified_by;

    private String fax;

    private String gpo_box;

    private String address;

    @Lob
    private String h_description;

    private String h_image;
    
    private String role;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private City city;


}
