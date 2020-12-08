package backendapplication.service;

import dto.BookingDTO;
import dto.CreateBookingDTO;
import backendapplication.logiclayer.logicHandler;
import service.BookingUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingHandler implements BookingUtility {
    private static logicHandler logichandler = new logicHandler();
    private static RabbitProducer rabbitProducer = new RabbitProducer();

    @Override
    public boolean createBooking(CreateBookingDTO createBookingDTO) {
        boolean booking = logichandler.createBooking(createBookingDTO);
        if (!booking) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String message = "failed to create booking in system for " + createBookingDTO.getPassportNumber()
                    + " at " + createBookingDTO.getArrival() + " and " + createBookingDTO.getDeparture()
                    + " for rooms: " + createBookingDTO.getRoomNumbers() + " at time: " + timeStamp;
            try {
                rabbitProducer.createQueueSendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("sent message: " + message);
        }
        return booking;
    }

    @Override
    public boolean cancelBooking(int i) {
        return logichandler.cancelBooking(i);
    }

    @Override
    public BookingDTO findBooking(int i) {
        return logichandler.findBooking(i);
    }
}
