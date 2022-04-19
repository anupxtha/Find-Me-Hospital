package com.findMeHospital.patientConfig;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.findMeHospital.dao.PatientRepo;
import com.findMeHospital.entities.Patient;

public class PatientDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PatientRepo patientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Patient patient = this.patientRepo.getPatientByUserName(username);

        if (patient == null) {
            throw new UsernameNotFoundException("Could not found user");
        }

        CustomPatientDetails cud = new CustomPatientDetails(patient);

        return cud;
    }

}
