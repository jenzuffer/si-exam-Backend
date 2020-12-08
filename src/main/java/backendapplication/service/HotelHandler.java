package backendapplication.service;
import dto.*;
import backendapplication.logiclayer.logicHandler;
import service.HotelUtility;

import java.util.Collection;
import java.util.Date;

public class HotelHandler implements HotelUtility {
    private logicHandler logichandler = new logicHandler();

    @Override
    public Collection<HotelDTO> findVacantHotels(VacantHotelsDTO vacantHotelsDTO) {
        String city = vacantHotelsDTO.getCity();
        Date date = vacantHotelsDTO.getDate();
        int numberOfGuests = vacantHotelsDTO.getNumberOfGuests();
        return logichandler.fetchHotels(city, date, numberOfGuests);
        //skal ikke retunere null , men tomt
    }
}
