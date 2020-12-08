package backendapplication.controller;

import backendapplication.service.HotelHandler;
import dto.HotelDTO;
import dto.VacantHotelsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
public class HotelController {
    HotelHandler hotelHandler = new HotelHandler();

    @GetMapping("/hotel")
    @ResponseBody
    public Collection<HotelDTO> findVacantHotels(@RequestParam String city, @RequestParam Date date, @RequestParam int numberOfGuests) {
        VacantHotelsDTO vacantHotelsDTO = new VacantHotelsDTO(city, date, numberOfGuests);
        return hotelHandler.findVacantHotels(vacantHotelsDTO);
    }
}
