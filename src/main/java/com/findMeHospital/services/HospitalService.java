package com.findMeHospital.services;

import com.findMeHospital.dao.*;
import com.findMeHospital.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {
    @Autowired
    private HospitalRepo hospitalRepo;

    public Hospital addHospital(Hospital hospital) {
        Hospital hos = this.hospitalRepo.save(hospital);
        return hos;
    }

    public List<Hospital> getHospital(){
        List<Hospital> allHospital = (List<Hospital>) this.hospitalRepo.findAll();
        return allHospital;
    }

    public Hospital getHospitalById(int id) {
        Hospital hospital = null;

        try {
            hospital = this.hospitalRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hospital;
    }

    public void deleteHospital(int hospitalId) {
        this.hospitalRepo.deleteById(hospitalId);
    }

    public List<Hospital> getHospitalByIdAndCityId(int hosId, int cityId){
        List<Hospital> hospitals = null;

        try {
            hospitals = this.hospitalRepo.findHospitalByIdAndCity_Id(hosId, cityId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hospitals;
    }

    public List<Integer> findHospitalByName(String hos){
        List<Integer> hospitals = this.hospitalRepo.searchHospital(hos);
        return hospitals;
    }
}
