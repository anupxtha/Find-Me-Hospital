package com.findMeHospital.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.findMeHospital.entities.Admin;
import com.findMeHospital.entities.Hospital;

public interface AdminRepo extends CrudRepository<Admin, Integer>{

	@Query("select u from Admin u where u.a_email = :email")
	public Admin getAdminByUserName(@Param("email") String email);
}
