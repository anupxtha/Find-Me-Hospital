package com.findMeHospital.dao;

import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepo extends CrudRepository<Hospital, Integer> {
    public Hospital findById(int id);
    
    @Query("SELECT u FROM Hospital u")
    public Page<Hospital> findAllHospital(Pageable pageable);

    @Query("SELECT u.id FROM Hospital u WHERE lower(u.h_name) LIKE %:name%")
    public List<Integer> searchHospital(@Param("name") String name);
    
    @Query("SELECT u FROM Hospital u WHERE lower(u.h_name) LIKE %:name%")
    public List<Hospital> searchHospitalByName(@Param("name") String name);
    
    @Query("SELECT u FROM Hospital u order by u.h_name asc")
    public List<Hospital> sortHospital();

    public List<Hospital> findHospitalByIdAndCity_Id(int hosId, int cityId);
    
	@Query("select u from Hospital u where u.h_email = :email")
	public Hospital getHospitalByUserName(@Param("email") String email);
	
	@Query("SELECT count(*) FROM Hospital u")
	public long countHospital();
}
