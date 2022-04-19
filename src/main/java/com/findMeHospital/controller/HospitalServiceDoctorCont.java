package com.findMeHospital.controller;

import com.findMeHospital.dao.*;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HospitalServiceDoctorCont {
    @Autowired
    private HospitalServiceDoctorSevice hospitalServiceDoctorSevice;

    @GetMapping("/getHSD")
    private ResponseEntity getHSD(){
        List<HospitalServiceDoctor> hospitalServiceDoctor = this.hospitalServiceDoctorSevice.getHospitalServiceDoctor();

        if (hospitalServiceDoctor.size() <= 0) {
        	return new ResponseEntity<>(new ResponseDto("HospitalsServicesDoctors Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully HospitalsServicesDoctors Fetched", "success", hospitalServiceDoctor), HttpStatus.OK);
    }

    @GetMapping("/getHSD/{id}")
    public ResponseEntity getHSDID(@PathVariable("id") int id) {
        HospitalServiceDoctor hospitalServiceDoctor = this.hospitalServiceDoctorSevice.getHSDById(id);

        if (hospitalServiceDoctor == null) {
        	return new ResponseEntity<>(new ResponseDto("HospitalServiceDoctor Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully HospitalServiceDoctor Fetched", "success", hospitalServiceDoctor), HttpStatus.OK);
    }

    @PostMapping("/saveHSD")
    private ResponseEntity saveHSD(@RequestBody HospitalServiceDoctor hospitalServiceDoctor) {

        HospitalServiceDoctor hsd = null;

        try {
            hsd = this.hospitalServiceDoctorSevice.addHospitalServiceDoctor(hospitalServiceDoctor);

            return new ResponseEntity<>(new ResponseDto("Successfully HospitalServiceDoctor saved", "success", hsd), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteHSD/{hsdId}")
    public ResponseEntity deleteHSD(@PathVariable("hsdId") int hsdId) {
        try {
            this.hospitalServiceDoctorSevice.deleteHospitalServiceDoctor(hsdId);
            return new ResponseEntity<>(new ResponseDto("Successfully HospitalServiceDoctor deleted", "success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
