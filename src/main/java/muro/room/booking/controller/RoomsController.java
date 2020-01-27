package muro.room.booking.controller;

import muro.room.booking.entity.Room;
import muro.room.booking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomsController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable("id") Long id) {
        return roomRepository.findById(id).get();
    }

    @PostMapping()
    public Room newRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @PutMapping()
    public Room updateRoom(@RequestBody Room updatedRoom) {
        Room originalRoom = roomRepository.findById(updatedRoom.getId()).get();
        originalRoom.setName(updatedRoom.getName());
        originalRoom.setLocation(updatedRoom.getLocation());
        originalRoom.setCapacities(updatedRoom.getCapacities());
        return roomRepository.save(originalRoom);
    }

}
