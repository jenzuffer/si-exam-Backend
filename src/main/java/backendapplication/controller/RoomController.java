package backendapplication.controller;

import backendapplication.service.RoomHandler;
import dto.RoomDTO;
import dto.VacantRoomsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class RoomController {
    RoomHandler roomHandler = new RoomHandler();

    @GetMapping("/room")
    @ResponseBody
    public Collection<RoomDTO> findVacantRooms(@RequestBody VacantRoomsDTO vacantRoomsDTO ){
        return roomHandler.findVacantRooms(vacantRoomsDTO);
    }

    @PutMapping("/room")
    public boolean markRoomAsReserved(@RequestBody List<String> list) {
        return false;
    }
}
