package com.findMeHospital.dao;

import com.findMeHospital.entities.Room;
import com.findMeHospital.entities.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepo extends CrudRepository<Room, Integer> {
    public Room findById(int id);
    
    @Query("SELECT u FROM Room u WHERE u.hospital.id = :hId")
    public Page<Room> findAllRoomsByHosId(@Param("hId") int hId, Pageable pageable);
    
    @Query("SELECT distinct(u.service) FROM Room u WHERE u.hospital.id = :hId")
    public Page<Service> findAllServicesByHosId(@Param("hId") int hId, Pageable pageable);

    @Query("SELECT u FROM Room u WHERE u.service.id = :sId and u.hospital.id = :hId")
    public List<Room> findRoomByServiceId(@Param("sId") int sId, @Param("hId") int hId);

    @Query("SELECT distinct(u.service) FROM Room u WHERE u.hospital.id = :hId")
    public List<Service> findServiceByHospitalId(@Param("hId") int hId);
    
    @Query("SELECT count(u.service) FROM Room u WHERE u.hospital.id = :hId")
    public long countRoomByHospital(@Param("hId") int hId);
}
