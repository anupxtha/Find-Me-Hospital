package com.findMeHospital.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.City;
import com.findMeHospital.entities.Doctor;
import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.HospitalServiceDoctor;
import com.findMeHospital.entities.Patient;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.PatientService;

@Controller
public class registerPatient {

	@Autowired
	private CityService cityService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/signup")
	public String registerPatients(Model model) {
		
		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);
		
		return "patient/signup";
	}


	// Posting Patient ////////////////////
		@RequestMapping(value = "/postPatient", method = RequestMethod.POST)
		public String postPatient(@ModelAttribute("patient") Patient patient,
				@RequestParam("dateofbirth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateofbirth, HttpSession session) {

			try {
				patient.setDate_of_birth(dateofbirth);
				patient.setRole("ROLE_PATIENT");
				patient.setP_password(passwordEncoder.encode(patient.getP_password()));
				
				this.patientService.addPatient(patient);
				
				session.setAttribute("msg", new ResponseDto("Register Successfully !!", "success"));

			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
			}

			return "redirect:/signup";
		}
	
}
