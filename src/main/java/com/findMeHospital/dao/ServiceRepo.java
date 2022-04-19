package com.findMeHospital.dao;

import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.Room;
import com.findMeHospital.entities.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepo extends CrudRepository<Service, Integer> {
    public Service findById(int id);
    
    @Query("SELECT u FROM Service u")
    public Page<Service> findAllServices(Pageable pageable);

    @Query("SELECT u FROM Service u WHERE lower(u.s_name) LIKE %:title%")
    public List<Service> searchServiceByName(@Param("title") String title);
    
    @Query("SELECT u.id FROM Service u WHERE lower(u.s_name) LIKE %:title%")
    public List<Integer> searchService(@Param("title") String title);
    
    @Query("SELECT u FROM Service u order by u.s_name asc")
    public List<Service> sortService();
    
    @Query("SELECT count(*) FROM Service u")
    public long countService();
}
