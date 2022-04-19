package com.findMeHospital.dao;

import org.springframework.data.repository.CrudRepository;

import com.findMeHospital.entities.Country;

public interface CountryRepo extends CrudRepository<Country, Integer> {

}
