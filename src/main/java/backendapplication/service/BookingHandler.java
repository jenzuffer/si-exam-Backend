package backendapplication.service;

import dto.BookingDTO;
import dto.CreateBookingDTO;
import backendapplication.logiclayer.logicHandler;
import service.BookingUtility;

public class BookingHandler implements BookingUtility {
    private static logicHandler logichandler = new logicHandler();
    private static RabbitProducer rabbitProducer = new RabbitProducer();

    @Override
    public boolean createBooking(CreateBookingDTO createBookingDTO) {
        boolean booking = logichandler.createBooking(createBookingDTO);
        if (!booking) {
            String message = "failed to create booking in system";
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
