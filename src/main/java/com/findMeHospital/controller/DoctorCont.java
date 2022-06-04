package com.findMeHospital.controller;

import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
public class DoctorCont {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/getDoctor")
    private ResponseEntity getDoctor(){
        List<Doctor> doctor = this.doctorService.getDoctor();

        if (doctor.size() <= 0) {
        	return new ResponseEntity<>(new ResponseDto("Doctors Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Doctors Fetched", "success", doctor), HttpStatus.OK);
    }

    @GetMapping("/getDoctor/{id}")
    public ResponseEntity getDoctorID(@PathVariable("id") int id) {
        Doctor doctor = this.doctorService.getDoctorById(id);

        if (doctor == null) {
        	return new ResponseEntity<>(new ResponseDto("Doctor Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Doctor Fetched", "success", doctor), HttpStatus.OK);
    }

    @PostMapping("/saveDoctor")
    private ResponseEntity saveDoctor(@RequestBody Doctor doctor) {

        Doctor d = null;

        try {

            d = this.doctorService.addDoctor(doctor);

            return new ResponseEntity<>(new ResponseDto("Successfully Doctor saved", "success", d), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteDoctor/{doctorId}")
    public ResponseEntity deleteDoctor(@PathVariable("doctorId") int doctorId) {
        try {
            this.doctorService.deleteDoctor(doctorId);
            return new ResponseEntity<>(new ResponseDto("Successfully Doctor deleted", "success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
