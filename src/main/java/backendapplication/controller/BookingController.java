package backendapplication.controller;

import backendapplication.service.BookingHandler;
import dto.BookingDTO;
import dto.CreateBookingDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private BookingHandler bookingHandler = new BookingHandler();

    @DeleteMapping("/{bookingid}")
    public boolean cancelBooking(@PathVariable Integer bookingid){return bookingHandler.cancelBooking(bookingid);}

    @GetMapping("/{bookingid}")
    @ResponseBody
    public BookingDTO findBooking(@PathVariable Integer bookingid){return bookingHandler.findBooking(bookingid);}

    @PostMapping("")
    public boolean createBooking(@RequestBody CreateBookingDTO booking){return bookingHandler.createBooking(booking);}
}
