package com.findMeHospital.dao;

import com.findMeHospital.dto.AppointmentDto;
import com.findMeHospital.entities.Appointment;
import com.findMeHospital.entities.Doctor;
import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.Patient;
import com.findMeHospital.entities.Service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepo extends CrudRepository<Appointment, Integer> {
    public Appointment findById(int id);
    
    @Query("SELECT u FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE h.hospital.id = :n order by u.id desc")
    public Page<Appointment> getAppointmentByHosId(@Param("n") int id, Pageable pageable);
    
    @Query("SELECT u FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE h.hospital.id = :n order by u.id desc")
    public List<Appointment> getAppByHosId(@Param("n") int id);
    
    @Query("SELECT DISTINCT u.patient FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE h.hospital.id = :n order by u.id desc")
    public Page<Patient> getAppPatientByHosId(@Param("n") int id, Pageable pageable);
    
    @Query("SELECT DISTINCT u FROM Appointment u WHERE u.patient.id = :n order by u.id desc")
    public Page<Appointment> getAppPatientByPatId(@Param("n") int id, Pageable pageable);
    
    @Query(value = "select m.month, count(u.requested_appointment_date) as total from (select 1 as month union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all select 10 union all select 11 union all select 12) m left join `find-me-hospital`.appointment u on month(u.requested_appointment_date) = m.month group by m.month", nativeQuery = true)
    public List<Map<Integer, Integer>> appCountWithDate();
    
    @Query(value = "select s.s_name as service,count(h.service_id) as total from `find-me-hospital`.appointment a join `find-me-hospital`.hos_ser_doc h on a.hospital_service_doctor_id=h.id  join `find-me-hospital`.service s on s.id = h.service_id group by s.s_name", nativeQuery = true)
    public List<Map<String, Integer>> serCountWithDate();
    
    @Query(value = "select count(distinct(a.patient_id)) from `find-me-hospital`.appointment a join `find-me-hospital`.hos_ser_doc h on a.hospital_service_doctor_id=h.id where h.hospital_id = :hId", nativeQuery = true)
    public long patientCountInHospital(@Param("hId") int hId);
    
    @Query(value = "select s.s_name as service,count(h.service_id) as total from `find-me-hospital`.appointment a join `find-me-hospital`.hos_ser_doc h on a.hospital_service_doctor_id=h.id  join `find-me-hospital`.service s on s.id = h.service_id where h.hospital_id = :hId group by s.s_name", nativeQuery = true)
    public List<Map<String, Integer>> serCountByHosIdandDate(@Param("hId") int hId);
    
    @Query("SELECT DISTINCT(h.hospital) FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE u.patient.id = :n")
    public Page<Hospital> getHospitalByPatId(@Param("n") int id, Pageable pageable);
     
    @Query("SELECT DISTINCT(h.doctor) FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE u.patient.id = :n")
    public Page<Doctor> getDoctorByPatId(@Param("n") int id, Pageable pageable);
    
    @Query("SELECT DISTINCT(h.service) FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE u.patient.id = :n")
    public Page<Service> getServiceByPatId(@Param("n") int id, Pageable pageable);
    
    @Query("SELECT COUNT(DISTINCT(h.hospital)) FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE u.patient.id = :n")
    public long countHospitalByPatId(@Param("n") int pId);
    
    @Query("SELECT COUNT(DISTINCT(h.doctor)) FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE u.patient.id = :n")
    public long countDoctorByPatId(@Param("n") int pId);
    
    @Query("SELECT COUNT(DISTINCT(h.service)) FROM Appointment u JOIN HospitalServiceDoctor h on h.id=u.hospitalServiceDoctor.id WHERE u.patient.id = :n")
    public long countServiceByPatId(@Param("n") int pId);
    
    @Query(value = "select s.s_name as service,count(h.service_id) as total from `find-me-hospital`.appointment a join `find-me-hospital`.hos_ser_doc h on a.hospital_service_doctor_id=h.id  join `find-me-hospital`.service s on s.id = h.service_id where a.patient_id = :pId group by s.s_name", nativeQuery = true)
    public List<Map<String, Integer>> serCountByPatIdandDate(@Param("pId") int pId);
}
