package com.findMeHospital.services;

import com.findMeHospital.dao.*;
import com.findMeHospital.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepo doctorRepo;

    public Doctor addDoctor(Doctor doctor) {
        Doctor doc = this.doctorRepo.save(doctor);
        return doc;
    }

    public List<Doctor> getDoctor(){
        List<Doctor> allDoctor = (List<Doctor>) this.doctorRepo.findAll();
        return allDoctor;
    }

    public Doctor getDoctorById(int id) {
        Doctor doctor = null;

        try {
            doctor = this.doctorRepo.findByNmc_no(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doctor;
    }

    public void deleteDoctor(int doctorId) {
        this.doctorRepo.deleteById(doctorId);
    }
}
