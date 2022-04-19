package com.findMeHospital.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.findMeHospital.dao.HospitalRepo;
import com.findMeHospital.dao.HospitalServiceDoctorRepo;
import com.findMeHospital.dao.ServiceRepo;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.services.CityService;
import com.findMeHospital.services.HospitalService;
import com.findMeHospital.services.HospitalServiceDoctorSevice;
import com.findMeHospital.services.ServiceService;

@Controller
public class searchHospitals_2 {

	@Autowired
	private CityService cityService;

	@Autowired
	private ServiceService serviceService;

	@Autowired
	private ServiceRepo serviceRepo;

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private HospitalRepo hospitalRepo;

	@Autowired
	private HospitalServiceDoctorSevice hospitalServiceDoctorSevice;

	@Autowired
	private HospitalServiceDoctorRepo hospitalServiceDoctorRepo;

	@PostMapping("/searchHospitals")
	public String hospitals(@RequestParam("service") String service, @RequestParam("city") int city) {
		City c = this.cityService.getCityById(city);
		return "redirect:findHospitals/" + service + "/" + c.getName();
	}

	@GetMapping("/findHospitals/{service}/{city}")
	private String searchingHospital(@PathVariable("service") String service, @PathVariable("city") String city,
			Model model) {
		ResponseDto o = this.getHospitals(service, city);

		model.addAttribute("hospitalList", o);
		model.addAttribute("searchService", service);

		// model.addAttribute( "serviceList" ,this.serviceService.getService());

		return "allHospitals";
	}

	private ResponseDto getHospitals(String service, String city) {

		List<Hospital> hospitals = new ArrayList<Hospital>();

		List<Integer> serviceId = this.serviceRepo.searchService(service);

		if (serviceId.size() <= 0) {
			return new ResponseDto("Service Not Found", "warning");
		}

		List<Integer> citiesId = this.cityService.getCityId(city);

		if (citiesId.size() <= 0) {
			return new ResponseDto("City Not Found", "warning");
		}

		List<Integer> hospitalId = new ArrayList<Integer>();

		for (int serId : serviceId) {
			Iterator<Integer> it = this.hospitalServiceDoctorSevice.getHospitalByServiceId(serId).iterator();
			while (it.hasNext()) {
				final Integer i = it.next();
				hospitalId.add(i);
			}
//            hospitalId = this.hospitalServiceDoctorSevice.getHospitalByServiceId(serId);
		}

		if (hospitalId == null) {
			return new ResponseDto("Hospital with requested Service Not Found", "warning");
		}

		for (Integer HosId : hospitalId) {
			if (HosId != null) {
				for (int cityId : citiesId) {
					List<Hospital> hospital = this.hospitalService.getHospitalByIdAndCityId(HosId, cityId);
					hospitals.addAll(hospital);
				}
			}
		}

		if (hospitals == null) {
			return new ResponseDto("Hospitals Not Found within requested city", "warning");
		}

		return new ResponseDto("Successfully Found", "success", hospitals);
	}

	@GetMapping("/allHospitals")
	public String allhospitals(Model model) {

		List<Hospital> h = this.hospitalService.getHospital();
		model.addAttribute("hospitalList", h);

		return "hospitals";
	}

	@GetMapping("/searchHospital")
	public String searchhospitals(Model model, @RequestParam("hospitalName") String hospitalName) {

		List<Hospital> h = this.hospitalRepo.searchHospitalByName(hospitalName);
		model.addAttribute("hospitalList", h);

		return "hospitalName";
	}

	@GetMapping("/sortHospital")
	public String sorthospitals(Model model) {

		List<Hospital> h = this.hospitalRepo.sortHospital();
		model.addAttribute("hospitalList", h);

		return "hospitalSort";
	}

	@GetMapping("/showDetails/{id}")
	public String sortDetails(Model model, @PathVariable("id") int id) {

		Hospital h = this.hospitalService.getHospitalById(id);
		model.addAttribute("hospital", h);

		return "hospitalDetail";
	}

	@GetMapping("/ServiceOfHospital/{id}")
	public String getHospitalByServiceId(@PathVariable("id") int id, Model model) {

		List<Hospital> h = this.hospitalServiceDoctorRepo.getHospitalsByServiceId(id);

		model.addAttribute("serviceDetail", this.serviceService.getServiceById(id));
		model.addAttribute("hospitalList", h);

		return "ServiceOfHospital";
	}
}
