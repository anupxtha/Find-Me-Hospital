package com.findMeHospital.services;

import com.findMeHospital.dao.*;
import com.findMeHospital.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalServiceDoctorSevice {
    @Autowired
    private HospitalServiceDoctorRepo hospitalServiceDoctorRepo;

    public HospitalServiceDoctor addHospitalServiceDoctor(HospitalServiceDoctor hospitalServiceDoctor) {
        HospitalServiceDoctor hsd = this.hospitalServiceDoctorRepo.save(hospitalServiceDoctor);
        return hsd;
    }

    public List<HospitalServiceDoctor> getHospitalServiceDoctor(){
        List<HospitalServiceDoctor> allHSD = (List<HospitalServiceDoctor>) this.hospitalServiceDoctorRepo.findAll();
        return allHSD;
    }

    public HospitalServiceDoctor getHSDById(int id) {
        HospitalServiceDoctor hsd = null;

        try {
            hsd = this.hospitalServiceDoctorRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hsd;
    }

    public void deleteHospitalServiceDoctor(int hsdId) {
        this.hospitalServiceDoctorRepo.deleteById(hsdId);
    }

    public List<Integer> getHospitalByServiceId(int id){
        List<Integer> hospitalId = null;

        try {
            hospitalId = this.hospitalServiceDoctorRepo.getHospitalByServiceId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hospitalId;
    }

    public List<Integer> getDoctorByHosIdSerId(int sId, int hId){
        List<Integer> doctorId = null;
        try {
            doctorId = this.hospitalServiceDoctorRepo.getDoctorsByHospitalIdAndServiceId(sId, hId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctorId;
    }

    public List<HospitalServiceDoctor> getHSDByHSD(int hId, int sId, int dId){
        List<HospitalServiceDoctor> hsdList = null;
        try {
            hsdList = this.hospitalServiceDoctorRepo.getHSDByHosIdSerIdDocId(hId, sId, dId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hsdList;
    }
}
