package com.findMeHospital.services;

import com.findMeHospital.dao.CityRepo;
import com.findMeHospital.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepo cityRepo;

    public List<City> getCity(){
        List<City> allCity = (List<City>) this.cityRepo.findAll();
        return allCity;
    }

    public List<Integer> getCityId(String name){
        List<Integer> id = this.cityRepo.searchCityId(name);
        return id;
    }

    public City getCityById(int id) {
        City city = null;

        try {
            city = this.cityRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return city;
    }
}
