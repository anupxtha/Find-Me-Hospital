package com.findMeHospital.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "hos_ser_doc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalServiceDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    private Doctor doctor;
}
