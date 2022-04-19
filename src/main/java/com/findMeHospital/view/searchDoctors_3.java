package com.findMeHospital.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.findMeHospital.dao.DoctorRepo;
import com.findMeHospital.dao.HospitalServiceDoctorRepo;
import com.findMeHospital.dao.ServiceRepo;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.services.*;

@Controller
public class searchDoctors_3 {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private ServiceRepo serviceRepo;

	@Autowired
	private HospitalServiceDoctorSevice hospitalServiceDoctorSevice;

	@Autowired
	private HospitalServiceDoctorRepo hospitalServiceDoctorRepo;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private DoctorRepo doctorRepo;

	@GetMapping("/searchDoctor")
	public String doctors(@RequestParam(value = "hospital") String hospital,
			@RequestParam(value = "service") String service, Model model) {

		ResponseDto rd = this.getDoctors(hospital, service);

		if (rd != null) {
			model.addAttribute("docList", rd);
		} else {
			model.addAttribute("docList", 0);
		}
		model.addAttribute("hos", hospital);
		model.addAttribute("ser", service);

		return "allDoctors";
	}

	private ResponseDto getDoctors(String hospital, String service) {

		List<Integer> docId = new ArrayList<Integer>();

		List<Doctor> doctors = new ArrayList<Doctor>();

		List<Integer> hosId = this.hospitalService.findHospitalByName(hospital);

		List<Integer> serId = this.serviceRepo.searchService(service);

		for (int hId : hosId) {
			for (int sId : serId) {
				Iterator<Integer> it = this.hospitalServiceDoctorSevice.getDoctorByHosIdSerId(sId, hId).iterator();
				while (it.hasNext()) {
					final Integer i = it.next();
					docId.add(i);
				}
//                docId = this.hospitalServiceDoctorSevice.getDoctorByHosIdSerId(sId, hId);
			}
		}

		for (Integer dId : docId) {
			if (dId != null) {
				Doctor doctor = this.doctorService.getDoctorById(dId);
				doctors.add(doctor);
			}
		}

		if (doctors.size() <= 0) {
			return new ResponseDto("Doctors in associated services in selected hospital not found", "warning");
		}

		return new ResponseDto("Successfully Data found", "success", doctors);
	}

	@GetMapping("/allDoctors")
	public String allDoctors(Model model) {

		List<Doctor> d = this.doctorService.getDoctor();
		model.addAttribute("doctorList", d);

		return "doctors";
	}

	@GetMapping("/searchDoctors")
	public String searchDoctors(Model model, @RequestParam("DoctorName") String DoctorName) {

		List<Doctor> d = this.doctorRepo.searchDoctorByName(DoctorName);
		model.addAttribute("doctorList", d);

		return "doctorName";
	}

	@GetMapping("/sortDoctors")
	public String sortDoctors(Model model) {

		List<Doctor> d = this.doctorRepo.sortDoctor();
		model.addAttribute("doctorList", d);

		return "doctorSort";
	}

	@GetMapping("/showDoctorDetails/{id}")
	public String showDoctorDetails(Model model, @PathVariable("id") int id) {

		Doctor d = this.doctorService.getDoctorById(id);
		model.addAttribute("doctor", d);

		return "doctorDetail";
	}

	@GetMapping("/DoctorOfHospital/{id}")
	public String getHospitalByDoctorId(@PathVariable("id") int id, Model model) {

		List<Hospital> h = this.hospitalServiceDoctorRepo.getHospitalsByDoctorId(id);

		model.addAttribute("doctorDetails", this.doctorService.getDoctorById(id));
		model.addAttribute("hospitalList", h);

		return "DoctorOfHospital";
	}

}
