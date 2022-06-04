package com.findMeHospital.view;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.findMeHospital.adminConfig.SecurityConfigs;
import com.findMeHospital.dao.AdminRepo;
import com.findMeHospital.dao.AppointmentRepo;
import com.findMeHospital.dao.CityRepo;
import com.findMeHospital.dao.CountryRepo;
import com.findMeHospital.dao.DoctorRepo;
import com.findMeHospital.dao.HospitalRepo;
import com.findMeHospital.dao.PatientRepo;
import com.findMeHospital.dao.ServiceRepo;
import com.findMeHospital.dto.AppointmentDto;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.patientConfig.SecurityConfigss;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.HospitalService;
import com.findMeHospital.services.PatientService;
import com.findMeHospital.services.ServiceService;

@Controller
@RequestMapping("/admin")
public class adminDashboard {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private CityService cityService;

	@Autowired
	private CityRepo cityRepo;

	@Autowired
	private CountryRepo countryRepo;

	@Autowired
	private ServiceService serviceService;

	@Autowired
	private ServiceRepo serviceRepo;

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private HospitalRepo hospitalRepo;

	@Autowired
	private DoctorRepo doctorRepo;

	@Autowired
	private AppointmentRepo appointmentRepo;

	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private PatientRepo patientRepo;

	@RequestMapping("/login")
	public String hospitalLogin(Model model, HttpSession session) {

		session.removeAttribute("authority");
		session.removeAttribute("patient");

		return "admin/login";
	}

	@RequestMapping("")
	public String admin(Model model, Principal principal, HttpSession session) {

		if (principal == null) {
			System.out.println("It null");
		} else {
			String userName = principal.getName();

			Admin a = this.adminRepo.getAdminByUserName(userName);

			long serviceCount = this.serviceRepo.count();

			long hospitalCount = this.hospitalRepo.count();

			long doctorCount = this.doctorRepo.count();

			long patientCount = this.patientRepo.count();

			List<Map<Integer, Integer>> app = this.appointmentRepo.appCountWithDate();

			List<Object> totalAppMonth = new ArrayList<Object>();

			for (Map<Integer, Integer> m : app) {
				totalAppMonth.add(m.get("total"));
			}

			List<Map<String, Integer>> ser = this.appointmentRepo.serCountWithDate();

			List<Object> serName = new ArrayList<Object>();
			List<Object> totalSer = new ArrayList<Object>();

			for (Map<String, Integer> m : ser) {
				serName.add(m.get("service"));
				totalSer.add(m.get("total"));
			}

			session.setAttribute("authority", true);

			session.setAttribute("logoutUrl", "admin");

			model.addAttribute("totalService", serviceCount);
			model.addAttribute("totalHospital", hospitalCount);
			model.addAttribute("totalDoctor", doctorCount);
			model.addAttribute("totalPatient", patientCount);
			model.addAttribute("totalAppointment", totalAppMonth);
			model.addAttribute("serName", serName);
			model.addAttribute("totalSer", totalSer);

		}

		return "admin/admin";
	}

	@RequestMapping("/addCity")
	public String addCity(Model model) {

		List<Country> c = (List<Country>) this.countryRepo.findAll();

		model.addAttribute("country", c);

		return "admin/addCity";
	}

	@RequestMapping("/addService")
	public String addService(Model model) {
		return "admin/addService";
	}

	@RequestMapping("/addHospital")
	public String addHospital(Model model) {
		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);
		return "admin/addHospital";
	}

	@RequestMapping("/viewCities/{page}")
	public String viewCities(@PathVariable("page") Integer page, Model model) {

		Pageable pageable = PageRequest.of(page, 7);

		Page<City> c = this.cityRepo.findAllCities(pageable);

		model.addAttribute("cities", c);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", c.getTotalPages());

		return "admin/viewCities";
	}

	@RequestMapping("/viewService/{page}")
	public String viewService(@PathVariable("page") Integer page, Model model) {

		Pageable pageable = PageRequest.of(page, 4);

		Page<Service> s = this.serviceRepo.findAllServices(pageable);

		model.addAttribute("services", s);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", s.getTotalPages());

		return "admin/viewService";
	}

	@RequestMapping("/viewHospital/{page}")
	public String viewHospital(@PathVariable("page") Integer page, Model model) {
		Pageable pageable = PageRequest.of(page, 7);

		Page<Hospital> h = this.hospitalRepo.findAllHospital(pageable);

		model.addAttribute("hospitals", h);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", h.getTotalPages());

		return "admin/viewHospital";
	}

	@RequestMapping("/viewPatient/{page}")
	public String viewPatients(@PathVariable("page") Integer page, Model model) {
		Pageable pageable = PageRequest.of(page, 7);

		Page<Patient> p = this.patientRepo.findAllPatient(pageable);

		model.addAttribute("patients", p);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", p.getTotalPages());

		return "admin/viewPatient";
	}

	// Posting City & Service & Hospital //
	// Posting Service ////////////////////
	@RequestMapping(value = "/postCity", method = RequestMethod.POST)
	public String postCity(@ModelAttribute("city") City city, HttpSession session) {

		try {

			this.cityRepo.save(city);

			session.setAttribute("msg", new ResponseDto("City Saved with Proper Info!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/admin/addCity";
	}

	// Posting Service ////////////////////
	@RequestMapping(value = "/postService", method = RequestMethod.POST)
	public String postService(@ModelAttribute("service") Service service, @RequestParam("image") MultipartFile file,
			HttpSession session) {

		try {

			if (file.isEmpty()) {
				this.serviceService.addService(service);
				session.setAttribute("msg", new ResponseDto("Service Saved with default Image !!", "warning"));
			} else {
				service.setS_image(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/services").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				this.serviceService.addService(service);

				session.setAttribute("msg", new ResponseDto("Service Saved with Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/admin/addService";
	}

	// Posting Hospital ////////////////////
	@RequestMapping(value = "/postHospital", method = RequestMethod.POST)
	public String postHospital(@ModelAttribute("hospital") Hospital hospital,
			@RequestParam("established_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date established,
			@RequestParam("image") MultipartFile file, HttpSession session) {

		try {

			hospital.setH_password(passwordEncoder.encode(hospital.getH_password()));
			hospital.setEstablished(established);
			hospital.setRole("ROLE_HOSPITAL");

			if (file.isEmpty()) {
				this.hospitalService.addHospital(hospital);
				session.setAttribute("msg", new ResponseDto("Hospital Saved with default Image !!", "warning"));
			} else {
				hospital.setH_image(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/hospitals").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				this.hospitalService.addHospital(hospital);

				session.setAttribute("msg", new ResponseDto("Hospital Saved with Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/admin/addHospital";
	}

	// show City
	@RequestMapping("/city/{id}")
	public String showCity(@PathVariable("id") Integer id, Model model) {

		List<Country> c = (List<Country>) this.countryRepo.findAll();

		model.addAttribute("country", c);
		model.addAttribute("city", cityService.getCityById(id));
		return "admin/showCity";
	}

	// show Services
	@RequestMapping("/service/{id}")
	public String showService(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("service", serviceService.getServiceById(id));
		return "admin/showService";
	}

	// show Hospital
	@RequestMapping("/hospital/{id}")
	public String showHospital(@PathVariable("id") Integer id, Model model) {
		List<City> cities = this.cityService.getCity();
		model.addAttribute("city", cities);

		model.addAttribute("hospital", hospitalService.getHospitalById(id));
		return "admin/showHospital";
	}

	// Update City ////////////////////
	@RequestMapping(value = "/updateCity", method = RequestMethod.POST)
	public String updateCity(@ModelAttribute("city") City city, HttpSession session) {

		try {
			this.cityRepo.save(city);

			session.setAttribute("msg", new ResponseDto("City Updated successfully !!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/admin/city/" + city.getId();
	}

	// Update Service ////////////////////
	@RequestMapping(value = "/updateService", method = RequestMethod.POST)
	public String updateService(@ModelAttribute("service") Service service, @RequestParam("image") MultipartFile file,
			HttpSession session) {

		try {

			if (file.isEmpty()) {

				Service s = this.serviceService.getServiceById(service.getId());

				service.setS_image(s.getS_image());

				this.serviceService.addService(service);

				session.setAttribute("msg", new ResponseDto("Service Updated with default Image !!", "warning"));
			} else {
				service.setS_image(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/services").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				this.serviceService.addService(service);

				session.setAttribute("msg", new ResponseDto("Service Updated with New Image !!", "success"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}

		return "redirect:/admin/service/" + service.getId();
	}

	// update Hospital ////////////////////
	@RequestMapping(value = "/updateHospital", method = RequestMethod.POST)
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

		return "redirect:/admin/hospital/" + hospital.getId();
	}
	
	@RequestMapping("/Patient/Patient_Id/{id}")
	public String PatientDetails(@PathVariable("id") int id, Model model) {

		model.addAttribute("patient", this.patientService.getPatientById(id));

		return "admin/PatientDetail";
	}
	
	@RequestMapping("/deleteCity/{cityId}")
	public String DeleteCity(@PathVariable("cityId") int id,  HttpSession session) {
		try {
			City c = this.cityRepo.findById(id);
			c.setCountry(null);
            this.cityRepo.deleteById(id);
            session.setAttribute("msg", new ResponseDto("City Successfully Deleted !!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
        }
		return "redirect:/admin/viewCities/"+0;
	}	
	
	@RequestMapping("/deleteService/{serviceId}")
	public String DeleteService(@PathVariable("serviceId") int id,  HttpSession session) {
		try {
            this.serviceRepo.deleteById(id);
            session.setAttribute("msg", new ResponseDto("Service Successfully Deleted !!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
        }
		return "redirect:/admin/viewService/"+0;
	}
	
	@RequestMapping("/deleteHospital/{hospitalId}")
	public String DeleteHospital(@PathVariable("hospitalId") int id,  HttpSession session) {
		try {
			Hospital h = this.hospitalService.getHospitalById(id);
			h.setCity(null);
			
            this.hospitalService.deleteHospital(id);
            session.setAttribute("msg", new ResponseDto("Hospital Successfully Deleted !!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
        }
		return "redirect:/admin/viewHospital/"+0;
	}

	
	@RequestMapping("/changePassword")
	public String changePassword(Model model) {

		return "admin/changePassword";
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword(Model model, Principal principal, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session) {

		try {
			SecurityConfigs s = new SecurityConfigs();
			
			String email = principal.getName();
			
			Admin admin = this.adminRepo.getAdminByUserName(email);
			
			if(s.passwordEncoderAdmin().matches(oldPassword, admin.getA_password())) {
				admin.setA_password(s.passwordEncoderAdmin().encode(newPassword));
				this.adminRepo.save(admin);
				session.setAttribute("msg", new ResponseDto("Password Successfully Changed !!", "success"));
			}else {
				session.setAttribute("msg", new ResponseDto("Old Password doesn't Match !!", "danger"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new ResponseDto("Something went wrong!!", "danger"));
		}
		return "redirect:/admin/changePassword";
	}
}
