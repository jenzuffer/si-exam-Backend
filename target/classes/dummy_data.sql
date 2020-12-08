INSERT INTO accomodations.hotelchain (code_three_letter, country_code) VALUES ("SCC","DK")

INSERT INTO accomodations.hotelchain (code_three_letter, country_code) VALUES ("RBS","DK")

INSERT INTO accomodations.hotelchain (code_three_letter, country_code) VALUES ("HMH","DK")

SELECT * FROM accomodations.hotelchain


INSERT INTO accomodations.hotel (hotel_number, name, adress, city, distance_to_center, star_rating, fk_hotel_chain)
VALUES (123456, "Scandic Sluseholmen", "Molestien 11", "København", 4, 4, "SCCDK")

INSERT INTO accomodations.hotel (hotel_number, name, adress, city, distance_to_center, star_rating, fk_hotel_chain)
VALUES (223456, "Radisson Blu Scandinavia", "Margrethepladsen 1", "Aarhus", 4, 1, "RBSDK")

INSERT INTO accomodations.hotel (hotel_number, name, adress, city, distance_to_center, star_rating, fk_hotel_chain)
VALUES (323456, "Helnan Marselis Hotel", "Strandvejen 25", "Aarhus", 4, 1, "HMHDK")

SELECT * FROM accomodations.hotel


INSERT INTO accomodations.room (room_number, capacity, is_vacant, roomtype, fk_hotel_number)
VALUES ("123S",4, 1, "S", 123456)

INSERT INTO accomodations.room (room_number, capacity, is_vacant, roomtype, fk_hotel_number)
VALUES ("123M",10, 1, "M", 123456)

INSERT INTO accomodations.room (room_number, capacity, is_vacant, roomtype, fk_hotel_number)
VALUES ("223S",4, 1, "D", 223456)

INSERT INTO accomodations.room (room_number, capacity, is_vacant, roomtype, fk_hotel_number)
VALUES ("223M",4, 1, "M", 223456)

INSERT INTO accomodations.room (room_number, capacity, is_vacant, roomtype, fk_hotel_number)
VALUES ("323S",5, 1, "T", 323456)

INSERT INTO accomodations.room (room_number, capacity, is_vacant, roomtype, fk_hotel_number)
VALUES ("323M",8, 1, "M", 323456)

SELECT * FROM accomodations.room r


insert into accomodations.booking (number_of_guest, fk_passport, late_arrival) Values(4, "123cmasd2", 0)

SELECT * FROM accomodations.booking b



insert into accomodations.roombooking (arival, departure, fk_booking_id, fk_room_number) values (curdate(), curdate(), 4, "123M")

SELECT * FROM accomodations.roombooking rb