package com.findMeHospital.controller;

import com.findMeHospital.dao.*;
import com.findMeHospital.dto.ResponseDto;
import com.findMeHospital.entities.*;
import com.findMeHospital.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomCont {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepo roomRepo;

    @GetMapping("/getRoom")
    private ResponseEntity getRoom(){
        List<Room> room = this.roomService.getRoom();

        if (room.size() <= 0) {
        	return new ResponseEntity<>(new ResponseDto("Rooms Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully rooms Fetched", "success", room), HttpStatus.OK);
    }

    @GetMapping("/getRoom/{id}")
    public ResponseEntity getRoomID(@PathVariable("id") int id) {
        Room room = this.roomService.getRoomById(id);

        if (room == null) {
        	return new ResponseEntity<>(new ResponseDto("Room Not Found", "warning"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto("Successfully room Fetched", "success", room), HttpStatus.OK);
    }

    @PostMapping("/saveRoom")
    private ResponseEntity saveRoom(@RequestBody Room room) {

        Room r = null;

        try {
            r = this.roomService.addRoom(room);

            return new ResponseEntity<>(new ResponseDto("Successfully Room saved", "success", r), HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteRoom/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable("roomId") int roomId) {
        try {
            this.roomService.deleteRoom(roomId);
            return new ResponseEntity<>(new ResponseDto("Successfully Room deleted", "success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDto("Something went Wrong in server", "danger"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findRoomByServiceId/{serId}/{hId}")
    public ResponseEntity getRoomByServiceId(@PathVariable("serId") int serId, @PathVariable("hId") int hId){
        List<Room> rooms = this.roomRepo.findRoomByServiceId(serId, hId);
        return new ResponseEntity<>(new ResponseDto("Successfully room found", "success", rooms), HttpStatus.OK);
    }
}
