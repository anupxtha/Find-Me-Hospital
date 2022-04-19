package com.findMeHospital.services;

import com.findMeHospital.dao.*;
import com.findMeHospital.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepo patientRepo;

    public Patient addPatient(Patient patient) {
        Patient pat = this.patientRepo.save(patient);
        return pat;
    }

    public List<Patient> getPatient(){
        List<Patient> allPatient = (List<Patient>) this.patientRepo.findAll();
        return allPatient;
    }

    public Patient getPatientById(int id) {
        Patient patient = null;

        try {
            patient = this.patientRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return patient;
    }

    public void deletePatient(int patientId) {
        this.patientRepo.deleteById(patientId);
    }
}
