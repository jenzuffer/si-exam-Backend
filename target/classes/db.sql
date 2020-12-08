create table accomodations.guest (passport varchar(255) not null , PRIMARY KEY(passport)) engine=innoDB;

create table accomodations.booking (booking_id integer  NOT NULL AUTO_INCREMENT, PRIMARY KEY(booking_id),
fk_passport varchar(255) not null, number_of_guest integer not null, late_arrival bool not null,
FOREIGN KEY (fk_passport) REFERENCES
accomodations.guest (passport)) engine=innoDB;

create table accomodations.hotelchain (country_code varchar(55) not null, code_three_letter varchar(55), PRIMARY KEY(code_three_letter))

create table accomodations.hotel (hotel_number integer not null, name varchar(255) not null, adress varchar(255) not null,
city varchar(255) not null, distance_to_center integer not null, star_rating integer not null, fk_hotel_chain varchar(255)
, primary key (hotel_number)) engine=innoDB;


create table accomodations.room(room_number varchar(255) not null, capacity integer not null, is_vacant boolean, roomtype varchar(255) not null,
fk_hotel_number integer not null,
FOREIGN KEY (fk_hotel_number) REFERENCES
accomodations.hotel (hotel_number), primary key (room_number)) engine=innoDB;



create table accomodations.roombooking(arival date not null, departure date not null, fk_room_number varchar(255) not null,
fk_booking_id integer not null, FOREIGN KEY (fk_booking_id) REFERENCES accomodations.booking (booking_id),
FOREIGN KEY (fk_room_number) REFERENCES accomodations.room (room_number)) engine=innoDB;