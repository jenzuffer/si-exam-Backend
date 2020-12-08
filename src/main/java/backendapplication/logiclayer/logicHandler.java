package backendapplication.logiclayer;

import backendapplication.datalayer.DataHandler;
import dto.*;

import java.util.Collection;
import java.util.Date;

public class logicHandler  {
    private DataHandler datahandler = new DataHandler();
    public logicHandler(){}

    public Collection<HotelDTO> fetchHotels(String s, Date date, int i) {
        return datahandler.fetchHotels(s, date, i);
    }

    public BookingDTO findBooking(int bookingid){return datahandler.findBooking(bookingid);}

    public boolean createBooking(CreateBookingDTO createBookingDTO) {
        return datahandler.createBooking(createBookingDTO);
    }

    public boolean cancelBooking(int i) {
        return datahandler.cancelBooking(i);
    }


    public Collection<RoomDTO> fetchRooms(VacantRoomsDTO vacantRoomsDTO) {
        return datahandler.fetchRooms(vacantRoomsDTO);
    }
}
