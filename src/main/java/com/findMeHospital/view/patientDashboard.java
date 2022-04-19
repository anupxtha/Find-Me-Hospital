package com.findMeHospital.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.findMeHospital.dao.AdminRepo;
import com.findMeHospital.dao.AppointmentRepo;
import com.findMeHospital.dao.HospitalRepo;
import com.findMeHospital.dao.PatientRepo;
import com.findMeHospital.dao.ServiceRepo;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.Admin;
import com.findMeHospital.entities.Appointment;
import com.findMeHospital.entities.City;
import com.findMeHospital.entities.Doctor;
import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.HospitalServiceDoctor;
import com.findMeHospital.entities.Patient;
import com.findMeHospital.entities.Room;
import com.findMeHospital.entities.Service;
import com.findMeHospital.patientConfig.SecurityConfigss;
import com.findMeHospital.services.AppointmentService;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.DoctorService;
import com.findMeHospital.services.HospitalService;
import com.findMeHospital.services.HospitalServiceDoctorSevice;
import com.findMeHospital.services.PatientService;
import com.findMeHospital.services.RoomService;
import com.findMeHospital.services.ServiceService;

@Controller
@RequestMapping("/patient")
public class patientDashboard {

	@Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private DoctorService doctorService;

	@Autowired
	private ServiceRepo serviceRepo;
	
	@Autowired
	private ServiceService serviceService;

	@Autowired
	private HospitalServiceDoctorSevice hospitalServiceDoctorSevice;

	@Autowired
	private PatientService patientService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private PatientRepo patientRepo;

	@Autowired
	private AppointmentRepo appointmentRepo;
	
	@Autowired
	private CityService cityService;

	private String message;

	@ModelAttribute
	public void commonModel(Model model, Principal principal) {

		if (principal != null) {
			// // It is common in everywhere to protect from error
			String userName = principal.getName();

			Patient p = this.patientRepo.getPatientByUserName(userName);

			model.addAttribute("user", p);
		}

		// // End
	}

	@RequestMapping("/signin")
	public String PatientLogin(Model model, HttpSession session, HttpServletRequest request) {

		session.removeAttribute("authority");
		session.removeAttribute("patient");

		String referrer = request.getHeader("Referer");

		if (referrer != null) {
			if (referrer.matches("(.*)takeAppointment(.*)")) {
				request.getSession().setAttribute("url_prior_login", referrer);
			}
		}

		return "patient/signin";
	}

	@RequestMapping("")
	public String Patient(Model model, Principal principal, HttpSession session) {
		if (principal == null) {
			System.out.println("It null");
		} else {
			String userName = principal.getName();

			Patient p = this.patientRepo.getPatientByUserName(userName);

			long HospitalCount = this.appointmentRepo.countHospitalByPatId(p.getId());
			long DoctorCount = this.appointmentRepo.countDoctorByPatId(p.getId());
			long ServiceCount = this.appointmentRepo.countServiceByPatId(p.getId());

			List<Map<String, Integer>> ser = this.appointmentRepo.serCountByPatIdandDate(p.getId());

			List<Object> serName = new ArrayList<Object>();
			List<Object> totalSer = new ArrayList<Object>();

			for (Map<String, Integer> m : ser) {
				serName.add(m.get("service"));
				totalSer.add(m.get("total"));
			}

			session.setAttribute("authority", true);

			session.setAttribute("logoutUrl", "patient");

			model.addAttribute("hospitalCount", HospitalCount);
			model.addAttribute("doctorCount", DoctorCount);
			model.addAttribute("serviceCount", ServiceCount);
			model.addAttribute("serName", serName);
			model.addAttribute("totalSer", totalSer);
		}
		return "patient/patient";
	}

	@PostMapping("/RequestAppointment")
	public String requestedAppointment(@RequestParam("hsdId") int hsdId,
			@RequestParam("RequestedDate") String RequestedDate, @RequestParam("message") String message) {

		this.message = message;

//		@DateTimeFormat(pattern = "dd/MM/yyyy h:mm a") Date
//		System.out.println(RequestedDate);
		return "redirect:/patient/AppointmentStatus/" + hsdId + '/' + RequestedDate;
	}

	@GetMapping("/AppointmentStatus/{hsdId}/{date}")
	public String appointmentReq(@PathVariable("hsdId") int hsdId, @PathVariable("date") String RequestedDate,
			Principal principal, HttpSession session) {

		try {
			if (principal == null) {
				System.out.println("It null");
			} else {
				String userName = principal.getName();

				Patient p = this.patientRepo.getPatientByUserName(userName);

				session.setAttribute("authority", true);

				session.setAttribute("logoutUrl", "patient");

				Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")).parse(RequestedDate);

				Appointment a = new Appointment();

				if (message != null) {
					a.setMessage(message);
				}

				a.setViewed(false);
				a.setRequested_appointment_date(date);
				a.setHospitalServiceDoctor(this.hospitalServiceDoctorSevice.getHSDById(hsdId));

				// patient id is manual so it must be come when patient login and from Principal
				a.setPatient(this.patientService.getPatientById(p.getId()));

				a.setA_status(0);

				Appointment app = this.appointmentService.addAppointment(a);

				session.setAttribute("msg",
						new ResponseDto("Appointment is Scheduled !! with -- " + app.getId(), "success"));

			}

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/patient/appointmentStatus/0";
	}

	@RequestMapping("/appointmentStatus/{page}")
	public String appointmentStatus(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Patient pat = this.patientRepo.getPatientByUserName(userName);
//		System.out.println(pat.getId());

		Pageable pageable = PageRequest.of(page, 7);

		Page<Appointment> a = this.appointmentRepo.getAppPatientByPatId(pat.getId(), pageable);

		model.addAttribute("appointments", a);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", a.getTotalPages());

		return "patient/appointmentStatus";
	}

//	@RequestMapping("/showAppointment/{id}")
//	public String appointmentEdit(@PathVariable("id") int id, Model model) {
//		model.addAttribute("appointment", this.appointmentService.getAppointmentById(id));
//		return "";
//	}

	@RequestMapping("/visitedHospital/{page}")
	public String visitedHospital(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Patient p = this.patientRepo.getPatientByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Hospital> h = this.appointmentRepo.getHospitalByPatId(p.getId(), pageable);

		model.addAttribute("hospitals", h);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", h.getTotalPages());

		return "patient/visitedHospital";
	}

	@RequestMapping("/visitedDoctor/{page}")
	public String visitedDoctor(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Patient p = this.patientRepo.getPatientByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Doctor> d = this.appointmentRepo.getDoctorByPatId(p.getId(), pageable);

		model.addAttribute("doctors", d);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", d.getTotalPages());

		return "patient/visitedDoctor";
	}

	@RequestMapping("/visitedService/{page}")
	public String visitedService(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Patient p = this.patientRepo.getPatientByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Service> s = this.appointmentRepo.getServiceByPatId(p.getId(), pageable);

		model.addAttribute("services", s);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", s.getTotalPages());

		return "patient/visitedService";
	}

	// // show Patient Info
	@RequestMapping("/info/{id}")
	public String showInfo(@PathVariable("id") int id, Model model, Principal principal) {
		String userName = principal.getName();

		Patient p = this.patientRepo.getPatientByUserName(userName);

		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);
		
		model.addAttribute("patient", p);

		return "patient/info";
	}

	// update Patient Info ////////////////////
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public String updatePatient(@ModelAttribute("patient") Patient patient,
			@RequestParam("dateofbirth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateofbirth,
			@RequestParam("image") MultipartFile file, HttpSession session, Principal principal) {

		try {

			String userName = principal.getName();

			Patient p = this.patientRepo.getPatientByUserName(userName);
			patient.setId(p.getId());
			patient.setDate_of_birth(dateofbirth);
			patient.setRole(p.getRole());
			patient.setP_email(p.getP_email());

			if (file.isEmpty()) {
				patient.setP_photo(p.getP_photo());
				this.patientService.addPatient(patient);
				session.setAttribute("msg", new ResponseDto("Patient Updated with default Image !!", "warning"));
			} else {
				patient.setP_photo(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/patients").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				this.patientService.addPatient(patient);

				session.setAttribute("msg", new ResponseDto("Patient Updated with New Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/patient/info/" + patient.getId();
	}
	
	@RequestMapping("/visitedDoctor/NMC_No/{id}")
	public String visitedDoctorNMCNo(@PathVariable("id") int id, Model model) {

		
		model.addAttribute("doctor", this.doctorService.getDoctorById(id));
		
		return "patient/visitedDoctorDetail";
	}
	
	@RequestMapping("/visitedService/Service_Id/{id}")
	public String visitedServiceDetails(@PathVariable("id") int id, Model model) {

		
		model.addAttribute("service", this.serviceService.getServiceById(id));
		
		return "patient/visitedServiceDetail";
	}
	
	@RequestMapping("/visitedHospital/Hospital_Id/{id}")
	public String visitedHospitalDetails(@PathVariable("id") int id, Model model) {

		
		model.addAttribute("hospital", this.hospitalService.getHospitalById(id));
		
		return "patient/visitedHospitalDetail";
	}
	
	@RequestMapping("/changePassword")
	public String changePassword(Model model) {

		return "patient/changePassword";
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword(Model model, Principal principal, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session) {

		try {
			SecurityConfigss s = new SecurityConfigss();
			
			String email = principal.getName();
			
			Patient patient = this.patientRepo.getPatientByUserName(email);
			
			if(s.passwordEncoderPatient().matches(oldPassword, patient.getP_password())) {
				patient.setP_password(s.passwordEncoderPatient().encode(newPassword));
				this.patientRepo.save(patient);
				session.setAttribute("msg", new ResponseDto("Password Successfully Changed !!", "success"));
			}else {
				session.setAttribute("msg", new ResponseDto("Old Password doesn't Match !!", "danger"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}
		return "redirect:/patient/changePassword";
	}
}
