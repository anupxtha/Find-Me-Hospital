package com.findMeHospital.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.findMeHospital.services.AppointmentService;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.DoctorService;
import com.findMeHospital.services.HospitalService;
import com.findMeHospital.services.HospitalServiceDoctorSevice;
import com.findMeHospital.services.PatientService;
import com.findMeHospital.services.RoomService;
import com.findMeHospital.services.ServiceService;
import com.findMeHospital.adminConfig.SecurityConfigs;
import com.findMeHospital.dao.AppointmentRepo;
import com.findMeHospital.dao.DoctorRepo;
import com.findMeHospital.dao.HospitalRepo;
import com.findMeHospital.dao.HospitalServiceDoctorRepo;
import com.findMeHospital.dao.RoomRepo;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.hospitalConfig.SecurityConfig;

@Controller
@RequestMapping("/hospital")
public class hospitalDashboard {
	
	@Autowired
	private PatientService patientService;

	@Autowired
	private ServiceService serviceService;

	@Autowired
	private CityService cityService;

	@Autowired
	private RoomRepo roomRepo;

	@Autowired
	private RoomService roomService;

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private HospitalRepo hospitalRepo;

	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private DoctorRepo doctorRepo;

	@Autowired
	private HospitalServiceDoctorSevice hospitalServiceDoctorSevice;

	@Autowired
	private HospitalServiceDoctorRepo hospitalServiceDoctorRepo;

	@Autowired
	private AppointmentRepo appointmentRepo;

	@Autowired
	private AppointmentService appointmentService;

	@ModelAttribute
	public void commonModel(Model model, Principal principal) {

		int viewCount = 0;
		if (principal != null) {
			// // It is common in everywhere to protect from error
			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

			List<Appointment> a = this.appointmentRepo.getAppByHosId(h.getId());

			for (Appointment app : a) {
				if (!app.getViewed()) {
					viewCount += 1;
				}
			}
			model.addAttribute("hospital", h);
		}

		model.addAttribute("totalAppointment", viewCount);

		// // End
	}

	@RequestMapping("/login")
	public String hospitalLogin(Model model, HttpSession session) {

		session.removeAttribute("authority");
		session.removeAttribute("patient");

		System.out.println(model.getAttribute("totalAppointment"));

		return "hospital/login";
	}

	@RequestMapping("")
	public String hospitalDashboards(Model model, Principal principal, HttpSession session) {

		if (principal == null) {
			System.out.println("It null");
		} else {
			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

			long serviceCount = this.hospitalServiceDoctorRepo.countServiceByHospital(h.getId());

			long roomCount = this.roomRepo.countRoomByHospital(h.getId());

			long doctorCount = this.hospitalServiceDoctorRepo.countDoctorByHospital(h.getId());

			long patientCount = this.appointmentRepo.patientCountInHospital(h.getId());

			List<Map<String, Integer>> ser = this.appointmentRepo.serCountByHosIdandDate(h.getId());

			List<Map<Integer, Integer>> app = this.appointmentRepo.appCountWithDateforHospital(h.getId());

			List<Object> totalAppMonth = new ArrayList<Object>();

			for (Map<Integer, Integer> m : app) {
				totalAppMonth.add(m.get("total"));
			}

			
			List<Object> serName = new ArrayList<Object>();
			List<Object> totalSer = new ArrayList<Object>();

			for (Map<String, Integer> m : ser) {
				serName.add(m.get("service"));
				totalSer.add(m.get("total"));
			}

			session.setAttribute("authority", true);

			session.setAttribute("logoutUrl", "hospital");

			model.addAttribute("totalService", serviceCount);
			model.addAttribute("totalRoom", roomCount);
			model.addAttribute("totalDoctor", doctorCount);
			model.addAttribute("totalPatient", patientCount);
			model.addAttribute("serName", serName);
			model.addAttribute("totalSer", totalSer);
			model.addAttribute("totalAppHospital", totalAppMonth);

		}

		return "hospital/hospital";
	}

//	public int viewCount(Principal principal) {
//		// // It is common in everywhere to protect from error
//		String userName = principal.getName();
//
//		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);
//
//		List<Appointment> a = this.appointmentRepo.getAppByHosId(h.getId());
//
//		int viewCount = 0;
//
//		for (Appointment app : a) {
//			if (!app.getViewed()) {
//				viewCount += 1;
//			}
//		}
//
//		return viewCount;
//
//		// // End
//	}

	@GetMapping("/addRoom")
	public String hospitalAddRoom(Model model, Principal principal) {
		model.addAttribute("serviceList", this.serviceService.getService());

//		model.addAttribute("totalAppointment", viewCount(principal));

		return "hospital/addRoom";
	}

	@GetMapping("/addDoctor")
	public String hospitalAddDoctor(Model model, Principal principal) {

		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);

		// passing hospital id manually need to pass from principle after login
		model.addAttribute("hId", h.getId());
		model.addAttribute("hospitalRoomService", this.roomRepo.findServiceByHospitalId(h.getId()));

//		model.addAttribute("totalAppointment", viewCount(principal));

		return "hospital/addDoctor";
	}

	@RequestMapping("/viewRoom/{page}")
	public String viewRoom(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Room> r = this.roomRepo.findAllRoomsByHosId(h.getId(), pageable);

//		model.addAttribute("totalAppointment", viewCount(principal));

		model.addAttribute("rooms", r);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", r.getTotalPages());

		return "hospital/viewRoom";
	}

	@RequestMapping("/viewService/{page}")
	public String viewService(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Service> s = this.roomRepo.findAllServicesByHosId(h.getId(), pageable);

//		model.addAttribute("totalAppointment", viewCount(principal));

		model.addAttribute("services", s);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", s.getTotalPages());

		return "hospital/viewService";
	}

	@RequestMapping("/viewDoctor/{page}")
	public String viewDoctor(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Doctor> d = this.hospitalServiceDoctorRepo.findAllDoctorsByHosId(h.getId(), pageable);

//		model.addAttribute("totalAppointment", viewCount(principal));

		model.addAttribute("doctors", d);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", d.getTotalPages());

		return "hospital/viewDoctor";
	}

	@RequestMapping("/viewAppointment")
	public String viewAppointment(@RequestParam(value = "page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Appointment> a = this.appointmentRepo.getAppointmentByHosId(h.getId(), pageable);

//		model.addAttribute("totalAppointment", viewCount(principal));

		model.addAttribute("appointments", a);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", a.getTotalPages());

		return "hospital/viewAppointment";
	}

	@RequestMapping("/viewPatient/{page}")
	public String viewUser(@PathVariable("page") Integer page, Principal principal, Model model) {

		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		Pageable pageable = PageRequest.of(page, 7);

		Page<Patient> p = this.appointmentRepo.getAppPatientByHosId(h.getId(), pageable);

//		model.addAttribute("totalAppointment", viewCount(principal));

		model.addAttribute("patients", p);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", p.getTotalPages());

		for (Patient patient : p) {
			System.out.println(patient.getP_photo());
		}

		return "hospital/viewPatient";
	}

	// Posting Room ////////////////////
	@RequestMapping(value = "/postRoom", method = RequestMethod.POST)
	public String postRoom(@ModelAttribute("room") Room room, Principal principal, HttpSession session) {

		try {
			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);
			// hospital id is manual so it must be come when hospital login and from
			// Principal
			room.setHospital(this.hospitalService.getHospitalById(h.getId()));
			// /////////

			this.roomService.addRoom(room);
			session.setAttribute("msg", new ResponseDto("Room saved with proper info!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/hospital/addRoom";
	}

	// Posting Doctor ////////////////////
	@RequestMapping(value = "/postDoctor", method = RequestMethod.POST)
	public String postDoctor(@ModelAttribute("doctor") Doctor doctor, Principal principal,
			@RequestParam("service") int service,
			@RequestParam("dateofbirth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateofbirth,
			@RequestParam("profile") MultipartFile file, HttpSession session) {

		try {
			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

			doctor.setDate_of_birth(dateofbirth);

			if (file.isEmpty()) {
				this.doctorService.addDoctor(doctor);
				session.setAttribute("msg", new ResponseDto("Doctor Saved with default Image !!", "warning"));
			} else {
				doctor.setD_photo(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/doctors").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				Doctor d = this.doctorService.addDoctor(doctor);

				HospitalServiceDoctor hsd = new HospitalServiceDoctor();

				// hospital id is manual so it must be come when hospital login and from
				// Principal
				hsd.setHospital(this.hospitalService.getHospitalById(h.getId()));
				//////////////////

				hsd.setDoctor(d);
				hsd.setService(this.serviceService.getServiceById(service));

				this.hospitalServiceDoctorSevice.addHospitalServiceDoctor(hsd);

				session.setAttribute("msg", new ResponseDto("Doctor Saved with Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Doctor with Email Already Exist!!", "danger"));
		}

		return "redirect:/hospital/addDoctor";
	}

	// // Updating Appointment
	@RequestMapping(value = "/updateAppointment", method = RequestMethod.POST)
	public String updateAppointment(@RequestParam(value = "page") int page,
			@RequestParam(value = "appointmentId") int apppointmentId, @RequestParam("a_status") int a_status) {
		Appointment a = this.appointmentRepo.findById(apppointmentId);

		a.setId(apppointmentId);

		a.setReview_appointment_date(new Date());

		if (a_status != 0) {
			a.setA_status(a_status);
			a.setViewed(true);
		}

		this.appointmentRepo.save(a);

		return "redirect:/hospital/viewAppointment?page=" + page;
	}

	// // show Doctor
	@RequestMapping("/doctor/{id}")
	public String showDoctor(@PathVariable("id") int id, Model model, Principal principal) {
		String userName = principal.getName();

		Hospital h = this.hospitalRepo.getHospitalByUserName(userName);

		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);

		model.addAttribute("doctor", this.doctorService.getDoctorById(id));

		// passing hospital id manually need to pass from principle after login
		model.addAttribute("hId", h.getId());
		model.addAttribute("hospitalRoomService", this.roomRepo.findServiceByHospitalId(h.getId()));

		return "hospital/showDoctor";
	}

	// // show Room
	@RequestMapping("/room/{id}")
	public String showRoom(@PathVariable("id") int id, Model model) {
		model.addAttribute("serviceList", this.serviceService.getService());
		model.addAttribute("room", this.roomService.getRoomById(id));
		return "hospital/showRoom";
	}

	// Update Hospital ////////////////////
	@RequestMapping(value = "/updateDoctor", method = RequestMethod.POST)
	public String updateDoctor(@ModelAttribute("doctor") Doctor doctor, Principal principal,
			@RequestParam("service") int service,
			@RequestParam("dateofbirth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateofbirth,
			@RequestParam("profile") MultipartFile file, HttpSession session) {

		try {

			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);
			doctor.setDate_of_birth(dateofbirth);

			if (file.isEmpty()) {
				Doctor d = this.doctorService.getDoctorById(doctor.getNmc_no());
				doctor.setD_photo(d.getD_photo());
				this.doctorService.addDoctor(doctor);
				session.setAttribute("msg", new ResponseDto("Doctor Updated with default Image !!", "warning"));
			} else {
				doctor.setD_photo(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/doctors").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				Doctor d = this.doctorService.addDoctor(doctor);

				session.setAttribute("msg", new ResponseDto("Doctor Updated with New Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/hospital/doctor/" + doctor.getNmc_no();
	}

	// Posting Room ////////////////////
	@RequestMapping(value = "/updateRoom", method = RequestMethod.POST)
	public String updateRoom(@ModelAttribute("room") Room room, Principal principal, HttpSession session) {

		try {
			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);
			// hospital id is manual so it must be come when hospital login and from
			// Principal
			room.setHospital(this.hospitalService.getHospitalById(h.getId()));
			// /////////

			this.roomService.addRoom(room);
			session.setAttribute("msg", new ResponseDto("Room Updated with proper info!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/hospital/room/" + room.getId();
	}

	// show Hospital Info
	@RequestMapping("/info/{id}")
	public String showHospital(@PathVariable("id") Integer id, Model model) {
		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);

		model.addAttribute("hospital", hospitalService.getHospitalById(id));
		return "hospital/info";
	}

	// update Hospital ////////////////////
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public String updateHospital(@ModelAttribute("hospital") Hospital hospital,
			@RequestParam("established_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date established,
			@RequestParam("image") MultipartFile file, HttpSession session) {

		try {

			hospital.setH_password(hospital.getH_password());

			hospital.setEstablished(established);
			hospital.setRole("ROLE_HOSPITAL");

			if (file.isEmpty()) {
				Hospital h = this.hospitalService.getHospitalById(hospital.getId());
				hospital.setH_image(h.getH_image());
				this.hospitalService.addHospital(hospital);
				session.setAttribute("msg", new ResponseDto("Hospital Update with default Image !!", "warning"));
			} else {
				hospital.setH_image(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/hospitals").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				this.hospitalService.addHospital(hospital);

				session.setAttribute("msg", new ResponseDto("Hospital Update with New Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/hospital/info/" + hospital.getId();
	}

	@RequestMapping("/Service/Service_Id/{id}")
	public String ServiceDetails(@PathVariable("id") int id, Model model) {

		model.addAttribute("service", this.serviceService.getServiceById(id));

		return "hospital/ServiceDetail";
	}

	@RequestMapping("/Patient/Patient_Id/{id}")
	public String PatientDetails(@PathVariable("id") int id, Model model) {

		model.addAttribute("patient", this.patientService.getPatientById(id));

		return "hospital/PatientDetail";
	}
	
	@RequestMapping("/deleteRoom/{roomId}")
	public String DeleteRoom(@PathVariable("roomId") int id,  HttpSession session) {
		try {
			Room r = this.roomRepo.findById(id);
			r.setService(null);
			r.setHospital(null);
            this.roomService.deleteRoom(id);
            session.setAttribute("msg", new ResponseDto("Room Successfully Deleted !!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
        }
		return "redirect:/hospital/viewRoom/"+0;
	}
	
	@RequestMapping("/deleteDoctor/{Id}")
	public String DeleteDoctor(@PathVariable("Id") int id,  HttpSession session, Principal principal) {
		try {
			String userName = principal.getName();

			Hospital h = this.hospitalRepo.getHospitalByUserName(userName);
			
			Doctor d = this.doctorService.getDoctorById(id);
			d.setRoom(null);
			d.setCity(null);

		    HospitalServiceDoctor hsd =	this.hospitalServiceDoctorRepo.getOnlyHSDByHosIdSerIdDocId(h.getId(), d.getNmc_no());
			this.hospitalServiceDoctorSevice.deleteHospitalServiceDoctor(hsd.getId());
		    this.doctorService.deleteDoctor(id);
            session.setAttribute("msg", new ResponseDto("Doctor Successfully Deleted !!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
        }
		return "redirect:/hospital/viewDoctor/"+0;
	}
	
	@RequestMapping("/changePassword")
	public String changePassword(Model model) {

		return "hospital/changePassword";
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword(Model model, Principal principal, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session) {

		try {
			SecurityConfig s = new SecurityConfig();
			
			String email = principal.getName();
			
			Hospital hospital = this.hospitalRepo.getHospitalByUserName(email);
		
			if(s.passwordEncoder().matches(oldPassword, hospital.getH_password())) {
				hospital.setH_password(s.passwordEncoder().encode(newPassword));
				this.hospitalRepo.save(hospital);
				session.setAttribute("msg", new ResponseDto("Password Successfully Changed !!", "success"));
			}else {
				session.setAttribute("msg", new ResponseDto("Old Password doesn't Match !!", "danger"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}
		return "redirect:/hospital/changePassword";
	}
}
