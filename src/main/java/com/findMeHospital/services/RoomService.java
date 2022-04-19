package com.findMeHospital.services;

import com.findMeHospital.dao.*;
import com.findMeHospital.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepo roomRepo;

    public Room addRoom(Room room) {
        Room r = this.roomRepo.save(room);
        return r;
    }

    public List<Room> getRoom(){
        List<Room> allRoom = (List<Room>) this.roomRepo.findAll();
        return allRoom;
    }

    public Room getRoomById(int id) {
        Room room = null;

        try {
            room = this.roomRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return room;
    }

    public void deleteRoom(int roomId) {
        this.roomRepo.deleteById(roomId);
    }
}
