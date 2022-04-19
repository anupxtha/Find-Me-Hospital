package com.findMeHospital.services;

import com.findMeHospital.dao.AppointmentRepo;
import com.findMeHospital.entities.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepo appointmentRepo;

	public Appointment addAppointment(Appointment appointment) {
		Appointment app = this.appointmentRepo.save(appointment);
		return app;
	}

	public List<Appointment> getAppointment() {
		List<Appointment> allAppointment = (List<Appointment>) this.appointmentRepo.findAll();
		return allAppointment;
	}

	public Appointment getAppointmentById(int id) {
		Appointment appointment = null;

		try {
			appointment = this.appointmentRepo.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return appointment;
	}

	public void deleteAppointment(int appointmentId) {
		this.appointmentRepo.deleteById(appointmentId);
	}

	public void updateAppointmentById(int id, Appointment appointment) {
		Appointment a = this.appointmentRepo.findById(id);

		a.setId(id);

		a.setReview_appointment_date(new Date());
		
		if (appointment.getA_status() > 0) {
			a.setA_status(appointment.getA_status());
			a.setViewed(true);
		}
		
		this.appointmentRepo.save(a);
	}
	
}
