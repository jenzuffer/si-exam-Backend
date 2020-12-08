package backendapplication.datalayer;



import backendapplication.controller.HomeController;
import dto.*;
import org.apache.log4j.Logger;
import types.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class DataHandler {
    private final static Logger logger = Logger.getLogger(DataHandler.class);
    private static DBSettings settings = new DBSettings();
    private static Connection con;


    private static void connect_to_db() throws SQLException {
        try {
            Class.forName(settings.getDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        con = DriverManager.getConnection(settings.getUrl(), settings.getUser(), settings.getDB_password());
        con.setAutoCommit(false);
        logger.info("Made connection to DB");
    }

    public DataHandler() {
        try {
            connect_to_db();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Collection<HotelDTO> fetchHotels(String s, Date date, int i) {
        Collection<HotelDTO> hotels = new ArrayList<>();
        String sql_capacity = "select capacity, fk_hotel_number from accomodations.room " +
                "r JOIN accomodations.hotel h ON h.city = ? where is_vacant = true " +
                "ORDER BY fk_hotel_number";
        int total_capacity_in_hotel = 0;
        String hotel_number_with_capacity = "";
        try (PreparedStatement ps = con.prepareStatement(sql_capacity)) {
            System.out.println(sql_capacity);
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            String prev_hotel_number = "";
            while (rs.next()) {
                int capacity = rs.getInt(1);
                String hotel_number = rs.getString(2);
                if (prev_hotel_number.isEmpty()) {
                    total_capacity_in_hotel = capacity;
                    prev_hotel_number = hotel_number;
                } else if (prev_hotel_number.equals(hotel_number)) {
                    total_capacity_in_hotel += capacity;
                }
                if (total_capacity_in_hotel >= i) {
                    hotel_number_with_capacity = hotel_number;
                    break;
                } else if (!prev_hotel_number.equals(hotel_number)) {
                    total_capacity_in_hotel = 0;
                }

                prev_hotel_number = hotel_number;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (hotel_number_with_capacity.isEmpty()) {
            logger.info("no hotel had enough room capacity for booking with: " + i
                    + " guests");
            return new ArrayList<HotelDTO>();
        }
        String sql = "SELECT name, adress, city, hotel_number FROM accomodations.roombooking rb INNER JOIN " +
                "accomodations.room r " +
                "ON rb.fk_room_number = r.room_number INNER JOIN " +
                "accomodations.hotel h ON r.fk_hotel_number = h.hotel_number WHERE ?" +
                "NOT BETWEEN rb.arival AND rb.departure  AND " +
                "h.city = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, (java.sql.Date) date);
            ps.setString(2, s);
            System.out.println(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String hotelname = rs.getString(1);
                String hoteladdress = rs.getString(2);
                String hotelcity = rs.getString(3);
                String hotel_number = rs.getString(4);
                if (hotel_number_with_capacity.equals(hotel_number)) {
                    HotelDTO hoteldto = new HotelDTO(hotelname, hoteladdress, hotelcity);
                    hotels.add(hoteldto);
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        commit();
        logger.info("found hotel with enough capacity");
        return hotels;
    }

    private static void commit() {
        try {
            con.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean createBooking(CreateBookingDTO createBookingDTO) {
        java.sql.Date arrival = new java.sql.Date(createBookingDTO.getArrival().getTime());
        java.sql.Date departure = new java.sql.Date(createBookingDTO.getDeparture().getTime());
        int numberOfGuests = createBookingDTO.getNumberOfGuests();
        String passportNumber = createBookingDTO.getPassportNumber();
        ArrayList<String> roomNumbers = createBookingDTO.getRoomNumbers();
        boolean lateArrival = createBookingDTO.isLateArrival();

        String sql = "select room_number from accomodations.room r where r.is_vacant = false";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String room_number = rs.getString(1);
                for (String roomNumber : roomNumbers) {
                    if (room_number.equals(roomNumber)) {
                        logger.info("did not create booking, rooms reserved already: " +
                                roomNumber);
                        //already reserved so no booking
                        return false;
                    }
                }
            }
        } catch (SQLException throwables) {
            logger.info("database error when creating booking");
            return false;
        }


        sql = "INSERT IGNORE INTO accomodations.guest (passport) values (?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, passportNumber);
            System.out.println(sql);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.info("database error when creating booking at inserting guest passport");
            return false;
        }
        int auto_generated_key = 0;
        sql = "INSERT INTO accomodations.booking (late_arrival, number_of_guest, fk_passport) values " +
                "(?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, lateArrival);
            ps.setInt(2, numberOfGuests);
            ps.setString(3, passportNumber);
            System.out.println(sql);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            auto_generated_key = generatedKeys.getInt(1);
        } catch (SQLException throwables) {
            logger.info("database error when creating booking at inserting booking");
            return false;
        }
        sql = "INSERT INTO accomodations.roombooking (arival, departure, fk_room_number, fk_booking_id) " +
                "values (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, (java.sql.Date) arrival);
            ps.setDate(2, (java.sql.Date) departure);
            ps.setInt(4, auto_generated_key);
            System.out.println(sql);
            for (String roomNumber : roomNumbers) {
                ps.setString(3, roomNumber);
                ps.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.info("database error when creating booking at inserting the room booking");
            return false;
        }
        //mangler hotel_number i CreateBookingDTO for at holde styr på hvilket hotel fra hvilken kæde
        sql = "UPDATE accomodations.room SET is_vacant = FALSE WHERE room_number = ? ";
        //"AND fk_hotel_number = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (String roomNumber : roomNumbers) {
                ps.setString(1, roomNumber);
                //ps.setDate(2, (java.sql.Date) departure);
                System.out.println(sql);
                ps.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.info("database error when creating booking at updating vacant rooms");
            return false;
        }
        commit();
        System.out.print("did create booking, rooms reserved now");
        return true;
    }

    public boolean cancelBooking(int BookingID) {
        String sql = "UPDATE accomodations.room r INNER JOIN accomodations.roombooking rb ON " +
                "r.room_number = rb.fk_room_number SET is_vacant = true " +
                "where rb.fk_booking_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            System.out.println(sql);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.info("database error cancelling booking at updating vacant rooms");
            return false;
        }

        sql = "delete from accomodations.roombooking where fk_booking_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            System.out.println(sql);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.info("database error cancelling booking at deleting from room bookins");
            return false;
        }
        sql = "delete from accomodations.booking WHERE booking_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            System.out.println(sql);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.info("database error cancelling booking at deleting booking");
            return false;
        }
        commit();
        return false;
    }



    public BookingDTO findBooking(int BookingID) {
        String sql = "select fk_passport, number_of_guest, late_arrival " +
                "from accomodations.booking b where booking_id = ?";
        BookingDTO bookingdto = new BookingDTO();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            System.out.println(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String passportNumber = rs.getString(1);
                int number_of_guest = rs.getInt(2);
                boolean late_arrival = rs.getBoolean(3);
                bookingdto.setPassportNumber(passportNumber);
                bookingdto.setNumberOfGuests(number_of_guest);
                bookingdto.setLateArrival(late_arrival);
            }
        } catch (SQLException throwables) {
            logger.info("error at finding booking");
            throwables.printStackTrace();
        }
        List<RoomBookingDTO> roomDTO_list = new ArrayList<RoomBookingDTO>();

        sql = "select adress, city, name from accomodations.hotel h " +
                "inner join accomodations.room r ON " +
                "h.hotel_number = r.fk_hotel_number INNER JOIN accomodations.roombooking rb ON " +
                "rb.fk_room_number = r.room_number JOIN accomodations.booking b ON b.booking_id = ?";
        HotelDTO hotelDTO = new HotelDTO();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            System.out.println(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String adress = rs.getString(1);
                String city = rs.getString(2);
                String name = rs.getString(3);
                hotelDTO.setAddress(adress);
                hotelDTO.setCity(city);
                hotelDTO.setName(name);
            }
        } catch (SQLException throwables) {
            logger.info("error at finding booking");
            throwables.printStackTrace();
        }

        sql = "select arival, departure, roomtype, room_number from accomodations.roombooking rb " +
                "inner join accomodations.room r ON r.room_number = rb.fk_room_number INNER JOIN " +
                "accomodations.booking b ON b.booking_id = rb.fk_booking_id " +
                "WHERE b.booking_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            System.out.println(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.sql.Date arival = rs.getDate(1);
                java.sql.Date departure = rs.getDate(2);
                String roomtype = rs.getString(3);
                String room_number = rs.getString(4);

                RoomBookingDTO roomBookingDTO = new RoomBookingDTO();
                RoomDTO roomDTO = new RoomDTO();

                roomDTO.setHotelDTO(hotelDTO);
                roomDTO.setRoomNumber(room_number);
                roomDTO.setRoomType(roomtype);

                roomBookingDTO.setRooms(roomDTO);
                roomBookingDTO.setArrivalDate(arival);
                roomBookingDTO.setDepartureDate(departure);
                roomDTO_list.add(roomBookingDTO);
            }
        } catch (SQLException throwables) {
            logger.info("error at finding booking");
            throwables.printStackTrace();
        }
        bookingdto.setRoomBookings(roomDTO_list);

        return bookingdto;
    }

    public Collection<RoomDTO> fetchRooms(VacantRoomsDTO vacantRoomsDTO) {
        return null;
    }
}

