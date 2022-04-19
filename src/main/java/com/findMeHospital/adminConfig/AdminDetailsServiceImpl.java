package com.findMeHospital.adminConfig;

import com.findMeHospital.dao.AdminRepo;
import com.findMeHospital.entities.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AdminDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = this.adminRepo.getAdminByUserName(username);

        if (admin == null) {
            throw new UsernameNotFoundException("Could not found user");
        }

        CustomAdminDetails cud = new CustomAdminDetails(admin);

        return cud;
    }

}
