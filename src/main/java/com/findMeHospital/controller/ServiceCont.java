package com.findMeHospital.controller;

import com.findMeHospital.entities.*;
import com.findMeHospital.services.*;
import com.findMeHospital.dto.*;
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
public class ServiceCont {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/getService")
    public ResponseEntity getService(){
        List<Service> service = this.serviceService.getService();

        if (service.size() <= 0) {
            return new ResponseEntity<>(new ResponseDto("Services Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Services Fetched", "success", service), HttpStatus.OK);
//        return ResponseEntity.status(HttpStatus.OK).body(service);
    }

    @GetMapping("/getService/{id}")
    public ResponseEntity getServiceID(@PathVariable("id") int id) {
        Service service = this.serviceService.getServiceById(id);

        if (service == null) {
        	return new ResponseEntity<>(new ResponseDto("Services Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Services Fetched", "success", service), HttpStatus.OK);
    }

    @PostMapping("/saveService")
    private ResponseEntity saveService(@RequestBody Service service) {

        Service s = null;

        try {
            s = this.serviceService.addService(service);

            return new ResponseEntity<>(new ResponseDto("Successfully Services saved", "success", s), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteService/{serviceId}")
    public ResponseEntity deleteService(@PathVariable("serviceId") int serviceId) {
        try {
            this.serviceService.deleteService(serviceId);
            return new ResponseEntity<>(new ResponseDto("Successfully Services deleted", "success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
