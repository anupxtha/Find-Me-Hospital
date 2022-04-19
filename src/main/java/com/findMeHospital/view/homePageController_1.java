package com.findMeHospital.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.findMeHospital.dao.AdminRepo;
import com.findMeHospital.dao.HospitalRepo;
import com.findMeHospital.dao.PatientRepo;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.Admin;
import com.findMeHospital.entities.City;
import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.Patient;
import com.findMeHospital.entities.Service;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.ServiceService;

@Controller
public class homePageController_1 {
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private PatientRepo patientRepo;
	
	@Autowired
	private HospitalRepo hospitalRepo;
	
	@Autowired
	private AdminRepo adminRepo;

	
	
	 @RequestMapping("/")
	 public String home(Model model) throws Exception {
		 
	      List<City> cities = this.cityService.getCity();
	      model.addAttribute("city", cities);
	      return "FindHospital";
	 }
	 
}
