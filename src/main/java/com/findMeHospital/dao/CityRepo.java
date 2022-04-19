package com.findMeHospital.dao;

import com.findMeHospital.entities.City;
import com.findMeHospital.entities.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepo extends CrudRepository<City, Integer> {

    public City findById(int id);

    @Query("SELECT u FROM City u")
    public Page<City> findAllCities(Pageable pageable);
    
    @Query("SELECT u.id FROM City u WHERE lower(u.name)  LIKE %:name%")
    public List<Integer> searchCityId(@Param("name") String name);
}
