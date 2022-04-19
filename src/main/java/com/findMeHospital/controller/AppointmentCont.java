package com.findMeHospital.controller;

import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppointmentCont {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/getAppointment")
    private ResponseEntity getAppointment(){
        List<Appointment> appointment = this.appointmentService.getAppointment();

        if (appointment.size() <= 0) {
        	return new ResponseEntity<>(new ResponseDto("Appointment Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Appointments Fetched", "success", appointment), HttpStatus.OK);
    }

    @GetMapping("/getAppointment/{id}")
    public ResponseEntity getAppointmentID(@PathVariable("id") int id) {
        Appointment appointment = this.appointmentService.getAppointmentById(id);

        if (appointment == null) {
        	return new ResponseEntity<>(new ResponseDto("Appointment Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully Appoinment Fetched", "success", appointment), HttpStatus.OK);
    }

    @PostMapping("/saveAppointment")
    private ResponseEntity saveAppointment(@RequestBody Appointment appointment) {

        Appointment a = null;

        try {
            a = this.appointmentService.addAppointment(appointment);

            return new ResponseEntity<>(new ResponseDto("Successfully Appointment saved", "success", a), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAppointment/{appointmentId}")
    public ResponseEntity deleteAppointment(@PathVariable("appointmentId") int appointmentId) {
        try {
            this.appointmentService.deleteAppointment(appointmentId);
            return new ResponseEntity<>(new ResponseDto("Successfully Appointment deleted", "success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/updateAppointment/{appointmentId}")
    public ResponseEntity updateAppointment(@RequestBody Appointment appointment ,@PathVariable("appointmentId") int appointmentId) {
    	try {
			this.appointmentService.updateAppointmentById(appointmentId, appointment);
			return new ResponseEntity<>(new ResponseDto("Successfully Appointment Updated", "success"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
}
