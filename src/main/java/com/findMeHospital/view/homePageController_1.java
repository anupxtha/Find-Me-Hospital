package com.findMeHospital.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Random;

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
import com.findMeHospital.patientConfig.SecurityConfigss;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.EmailService;
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

	@Autowired
	private EmailService emailService;

	Random random = new Random(1000);

	@RequestMapping("/")
	public String home(Model model) throws Exception {

		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);
		return "FindHospital";
	}

	@RequestMapping("/forgetPassword")
	public String forgetPassword(Model model) {

		return "forgetPassword";
	}

	@RequestMapping("/send-otp")
	public String sendopt(@RequestParam("email") String email, HttpSession session) {

		int otp = random.nextInt(999999);

		String subject = "OPT from Find-Me-Hospital";
		String message = "Your OTP Number is " + otp;
		String to = email;

		boolean flag = this.emailService.sendEmail(message, subject, to);

		if (flag) {
			session.setAttribute("otp", otp);
			session.setAttribute("femail", email);
			return "verify-otp";
		} else {
			session.setAttribute("msg", new ResponseDto("Check your email Id", "danger"));

			return "forgetPassword";
		}
	}

	@RequestMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {

		int MyOpt = (int) session.getAttribute("otp");
		String email = (String) session.getAttribute("femail");
		if (MyOpt == otp) {
			
			Patient patient = this.patientRepo.getPatientByUserName(email);
			
			if(patient == null) {
				session.setAttribute("msg", new ResponseDto("Patient Email doesn't Exist !!", "danger"));

				return "redirect:/forgetPassword";
			}else {
				
				return "changePassword";
			}
			
		} else {
			session.setAttribute("msg", new ResponseDto("Entered OPT is wrong !!", "danger"));
			return "verify-otp";
		}
	}
	
	@RequestMapping("/changePassword")
	public String changePassword(Model model, @RequestParam("password") String Password, HttpSession session) {

		try {
			SecurityConfigss s = new SecurityConfigss();
			
			String email = (String) session.getAttribute("femail");
			
			Patient patient = this.patientRepo.getPatientByUserName(email);
			
			patient.setP_password(s.passwordEncoderPatient().encode(Password));

			this.patientRepo.save(patient);
			
			session.setAttribute("msg", new ResponseDto("Password Changed Successfully!!", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}
		return "redirect:/patient/signin";
	}
	
}
