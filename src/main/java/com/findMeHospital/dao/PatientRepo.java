package com.findMeHospital.dao;

import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.Patient;
import com.findMeHospital.entities.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PatientRepo extends CrudRepository<Patient, Integer> {
    public Patient findById(int id);
    
    @Query("select u from Patient u where u.p_email = :email")
	public Patient getPatientByUserName(@Param("email") String email);
    
    @Query("SELECT u FROM Patient u")
    public Page<Patient> findAllPatient(Pageable pageable);
    
    @Query("Select count(*) from Patient u")
    public long countPatient();

}
