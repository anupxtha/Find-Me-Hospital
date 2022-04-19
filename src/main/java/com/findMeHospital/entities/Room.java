package com.findMeHospital.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String r_name;

    private String r_number;
    
    private String b_name;
    
    private String f_number;
    
    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Service service;
}
