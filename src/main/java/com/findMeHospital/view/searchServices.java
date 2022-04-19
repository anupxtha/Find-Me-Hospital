package com.findMeHospital.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.findMeHospital.dao.HospitalServiceDoctorRepo;
import com.findMeHospital.dao.ServiceRepo;
import com.findMeHospital.entities.Hospital;
import com.findMeHospital.entities.Service;
import com.findMeHospital.services.DoctorService;
import com.findMeHospital.services.HospitalService;
import com.findMeHospital.services.ServiceService;

@Controller
public class searchServices {
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private HospitalServiceDoctorRepo hospitalServiceDoctorRepo;
	
	@Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private ServiceRepo serviceRepo;
	
	@GetMapping("/allServices")
	public String allServices(Model model) {

		List<Service> s = this.serviceService.getService();
		model.addAttribute("servicesList", s);

		return "services";
	}
	
	
	@GetMapping("/searchService")
    public String searchService(Model model, @RequestParam("ServiceName") String ServiceName) {
    	
    	List<Service> s = this.serviceRepo.searchServiceByName(ServiceName);
    	model.addAttribute("servicesList", s);
    	
        return "serviceName";
    }
    
    @GetMapping("/sortServices")
    public String sortServices(Model model) {
    	
    	List<Service> s = this.serviceRepo.sortService();
    	model.addAttribute("servicesList", s);
    	
        return "sortService";
    }
    
    @GetMapping("/showServiceDetails/{id}")
    public String showServiceDetails(Model model, @PathVariable("id") int id) {
    	
    	Service s = this.serviceService.getServiceById(id);
    	model.addAttribute("service", s);
    	
        return "serviceDetail";
    }
    
    @GetMapping("/HospitalServices/{id}")
    public String getServicesByHospitalId(@PathVariable("id") int id, Model model) {
    	List<Service> s = this.hospitalServiceDoctorRepo.getServicesByHospitalId(id);
    	
    	model.addAttribute("hospitalDetail", this.hospitalService.getHospitalById(id));
		model.addAttribute("servicesList", s);
    	
    	return "HospitalService";
    }
    
    @GetMapping("/searchServiceByHosDoc")
	public String searchServiceByHosDoc(@RequestParam(value = "hospital") int hosId, @RequestParam(value = "doctor") int nmc_no,
	Model model) {
    	
    	List<Service> s = this.hospitalServiceDoctorRepo.getServiceByHospitalIdAndDoctorId(nmc_no, hosId);
    	model.addAttribute("hospitalDetails", this.hospitalService.getHospitalById(hosId));
    	model.addAttribute("doctorDetails", this.doctorService.getDoctorById(nmc_no));
    	model.addAttribute("servicesList", s);
    	return "HospitalDoctorService";
    }
}
