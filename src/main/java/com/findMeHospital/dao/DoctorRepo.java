package com.findMeHospital.dao;

import com.findMeHospital.entities.Doctor;
import com.findMeHospital.entities.Hospital;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DoctorRepo extends CrudRepository<Doctor, Integer> {

	@Query("SELECT u FROM Doctor u WHERE u.nmc_no = :id")
	public Doctor findByNmc_no(@Param("id") int id);

	@Query("SELECT u FROM Doctor u WHERE lower(u.d_name) LIKE %:name%")
	public List<Doctor> searchDoctorByName(@Param("name") String name);

	@Query("SELECT u FROM Doctor u order by u.d_name asc")
	public List<Doctor> sortDoctor();

	@Query("SELECT count(*) FROM Doctor u")
	public long countDoctor();
}
