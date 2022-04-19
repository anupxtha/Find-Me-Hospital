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
public class PatientCont {
    @Autowired
    private PatientService patientService;

    @GetMapping("/getPatient")
    private ResponseEntity getPatient(){
        List<Patient> patient = this.patientService.getPatient();

        if (patient.size() <= 0) {
        	return new ResponseEntity<>(new ResponseDto("Patients Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Patients Fetched", "success", patient), HttpStatus.OK);
    }

    @GetMapping("/getPatient/{id}")
    public ResponseEntity getPatientID(@PathVariable("id") int id) {
        Patient patient = this.patientService.getPatientById(id);

        if (patient == null) {
        	return new ResponseEntity<>(new ResponseDto("Patient Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Patient Fetched", "success", patient), HttpStatus.OK);
    }

    @PostMapping("/savePatient")
    private ResponseEntity savePatient(@RequestBody Patient patient) {

        Patient p = null;

        try {
//        	, @RequestParam("profile") MultipartFile file
//            if(file.isEmpty()){
//
//            }
//            else{
//                patient.setP_photo(file.getOriginalFilename());
//
//                File saveFile = new ClassPathResource("static/patients").getFile();
//
//                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
//
//                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//            }

            p = this.patientService.addPatient(patient);

            return new ResponseEntity<>(new ResponseDto("Successfully Patient saved", "success", p), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePatient/{patientId}")
    public ResponseEntity deletePatient(@PathVariable("patientId") int patientId) {
        try {
            this.patientService.deletePatient(patientId);
            return new ResponseEntity<>(new ResponseDto("Successfully Patient deleted", "success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
