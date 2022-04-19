package com.findMeHospital.hospitalConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.findMeHospital.dao.HospitalRepo;
import com.findMeHospital.entities.Hospital;

public class HospitalDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private HospitalRepo hospitalRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Hospital hospital = this.hospitalRepo.getHospitalByUserName(username);

        if (hospital == null) {
            throw new UsernameNotFoundException("Could not found user");
        }

        CustomHospitalDetails cud = new CustomHospitalDetails(hospital);

        return cud;
    }

}
