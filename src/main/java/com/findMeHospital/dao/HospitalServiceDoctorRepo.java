package com.findMeHospital.dao;

import com.findMeHospital.entities.Appointment;
import com.findMeHospital.entities.Doctor;
import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.HospitalServiceDoctor;
import com.findMeHospital.entities.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalServiceDoctorRepo extends CrudRepository<HospitalServiceDoctor, Integer> {
    public HospitalServiceDoctor findById(int id);

    @Query("select DISTINCT u.hospital.id from HospitalServiceDoctor u where u.service.id = :id")
    public List<Integer> getHospitalByServiceId(@Param("id") int id);
    
    @Query("SELECT distinct(u.doctor) FROM HospitalServiceDoctor u WHERE u.hospital.id = :hId")
    public Page<Doctor> findAllDoctorsByHosId(@Param("hId") int hId, Pageable pageable);

    @Query("select u.doctor.nmc_no from HospitalServiceDoctor u where u.service.id = :sId AND u.hospital.id= :hId")
    public List<Integer> getDoctorsByHospitalIdAndServiceId(@Param("sId") int sId, @Param("hId") int hId);

    @Query("select u from HospitalServiceDoctor u where u.hospital.id= :hId AND u.service.id = :sId AND u.doctor.nmc_no = :dId")
    public List<HospitalServiceDoctor> getHSDByHosIdSerIdDocId( @Param("hId") int hId, @Param("sId") int sId, @Param("dId") int dId);
    
    @Query("select u from HospitalServiceDoctor u where u.hospital.id= :hId AND u.doctor.nmc_no = :dId")
    public HospitalServiceDoctor getOnlyHSDByHosIdSerIdDocId( @Param("hId") int hId, @Param("dId") int dId);
    
    @Query("SELECT count(distinct(u.service)) FROM HospitalServiceDoctor u WHERE u.hospital.id = :hId")
    public long countServiceByHospital(@Param("hId") int hId);
   
    @Query("SELECT count(distinct(u.doctor)) FROM HospitalServiceDoctor u WHERE u.hospital.id = :hId")
    public long countDoctorByHospital(@Param("hId") int hId);
    
    @Query("select DISTINCT u.service from HospitalServiceDoctor u where u.hospital.id = :id")
    public List<Service> getServicesByHospitalId(@Param("id") int id);
    
    @Query("select DISTINCT u.hospital from HospitalServiceDoctor u where u.service.id = :id")
    public List<Hospital> getHospitalsByServiceId(@Param("id") int id);
    
    @Query("select DISTINCT u.hospital from HospitalServiceDoctor u where u.doctor.nmc_no = :id")
    public List<Hospital> getHospitalsByDoctorId(@Param("id") int id);
    
    @Query("select u.service from HospitalServiceDoctor u where u.doctor.nmc_no = :dId AND u.hospital.id= :hId")
    public List<Service> getServiceByHospitalIdAndDoctorId(@Param("dId") int dId, @Param("hId") int hId);
}
