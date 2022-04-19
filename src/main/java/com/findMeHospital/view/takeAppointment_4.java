package com.findMeHospital.view;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.findMeHospital.dao.ServiceRepo;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.HospitalServiceDoctor;
import com.findMeHospital.entities.Room;
import com.findMeHospital.services.HospitalService;
import com.findMeHospital.services.HospitalServiceDoctorSevice;
import com.findMeHospital.services.RoomService;

@Controller
public class takeAppointment_4 {

	@Autowired
	private ServiceRepo serviceRepo;

	@Autowired
	private HospitalServiceDoctorSevice hospitalServiceDoctorSevice;

	@Autowired
	private RoomService roomService;

	@Autowired
	private HospitalService hospitalService;

	@GetMapping("/takeAppointment")
	public String appointments(@RequestParam(value = "hospital") String hospital,
			@RequestParam(value = "service") String service, @RequestParam(value = "doctor") String doctor,
			@RequestParam(value = "docId") int docId, @RequestParam(value = "room") int roomId,
			Model model) {

		ResponseDto rd = this.receiveAppointment(hospital, service, docId);

		Room r = this.roomService.getRoomById(roomId);

		model.addAttribute("hsdList", rd);
		model.addAttribute("room", r);

		return "appointmentPage";
	}

	private ResponseDto receiveAppointment(String hospital, String service, int docId) {

		List<HospitalServiceDoctor> hsd = new ArrayList<HospitalServiceDoctor>();

		List<Integer> hosId = this.hospitalService.findHospitalByName(hospital);

		List<Integer> serId = this.serviceRepo.searchService(service);

		for (int hId : hosId) {
			for (int sId : serId) {
				hsd = this.hospitalServiceDoctorSevice.getHSDByHSD(hId, sId, docId);
			}
		}

		if (hsd.size() <= 0) {
			return new ResponseDto("Hospital, Service & Doctor not found in record", "warning");
		}

		return new ResponseDto("Successfully Data found", "success", hsd);
	}
}
