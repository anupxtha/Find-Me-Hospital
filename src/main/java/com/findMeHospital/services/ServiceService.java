package com.findMeHospital.services;

import com.findMeHospital.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.findMeHospital.entities.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepo serviceRepo;

    public com.findMeHospital.entities.Service addService(com.findMeHospital.entities.Service service) {
        com.findMeHospital.entities.Service ser = this.serviceRepo.save(service);
        return ser;
    }

    public List<com.findMeHospital.entities.Service> getService(){
        List<com.findMeHospital.entities.Service> allService = (List<com.findMeHospital.entities.Service>) this.serviceRepo.findAll();
        return allService;
    }

    public com.findMeHospital.entities.Service getServiceById(int id) {
        com.findMeHospital.entities.Service service = null;

        try {
            service = this.serviceRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return service;
    }

    public void deleteService(int serviceId) {
        this.serviceRepo.deleteById(serviceId);
    }

//    public List<com.hospital.entities.Service> searchService(String serviceName){
//        List<com.hospital.entities.Service> services = this.serviceRepo.searchService(serviceName);
//        return services;
//    }

}
