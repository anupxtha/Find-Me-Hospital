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
public class HospitalCont {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/getHospital")
    private ResponseEntity getHospital(){
        List<Hospital> hospital = this.hospitalService.getHospital();

        if (hospital.size() <= 0) {
        	return new ResponseEntity<>(new ResponseDto("Hospitals Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Hospitals Fetched", "success", hospital), HttpStatus.OK);
    }

    @GetMapping("/getHospital/{id}")
    public ResponseEntity getHospitalID(@PathVariable("id") int id) {
        Hospital hospital = this.hospitalService.getHospitalById(id);

        if (hospital == null) {
        	return new ResponseEntity<>(new ResponseDto("Hospital Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Hospital Fetched", "success", hospital), HttpStatus.OK);
    }

    @PostMapping("/saveHospital")
    private ResponseEntity saveHospital(@RequestBody Hospital hospital) {

        Hospital h = null;

        try {

            h = this.hospitalService.addHospital(hospital);

            return new ResponseEntity<>(new ResponseDto("Successfully Hospital saved", "success", h), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteHospital/{hospitalId}")
    public ResponseEntity deleteHospital(@PathVariable("hospitalId") int hospitalId) {
        try {
            this.hospitalService.deleteHospital(hospitalId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
