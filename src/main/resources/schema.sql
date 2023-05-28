DROP DATABASE IF EXISTS ticket_finder;

create database ticket_finder;
use ticket_finder;

DROP TABLE IF EXISTS artist;
CREATE TABLE artist (
  artist_id int AUTO_INCREMENT PRIMARY KEY,
  artist_name varchar(255) DEFAULT NULL,
  genre varchar(255) DEFAULT NULL,
  base_price float DEFAULT NULL,
  image varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS venue;
CREATE TABLE venue (
  venue_id int AUTO_INCREMENT PRIMARY KEY,
  venue_name varchar(255) DEFAULT NULL,
  capacity int DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  city varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS seat;
CREATE TABLE seat (
  seat_id int AUTO_INCREMENT PRIMARY KEY,
  seat_type varchar(255) DEFAULT NULL,
  seat_price int DEFAULT NULL
);

DROP TABLE IF EXISTS venue_seat;
CREATE TABLE venue_seat (
	venue_id int DEFAULT NULL,
	FOREIGN KEY (venue_id) REFERENCES venue(venue_id),
    seat_id int DEFAULT NULL,
	FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
	quantity int DEFAULT NULL
);

DROP TABLE IF EXISTS concert;
CREATE TABLE concert (
  concert_id int AUTO_INCREMENT PRIMARY KEY,
  artist_id int DEFAULT NULL,
  FOREIGN KEY (artist_id) REFERENCES artist(artist_id),
  venue_id int DEFAULT NULL,
  FOREIGN KEY (venue_id) REFERENCES venue(venue_id),
  c_date datetime DEFAULT NULL
);

DROP TABLE IF EXISTS tf_user;
CREATE TABLE tf_user (
  user_id int AUTO_INCREMENT PRIMARY KEY,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  user_password varchar(255) DEFAULT NULL,
  user_type varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS ticket;
CREATE TABLE ticket (
  ticket_id int AUTO_INCREMENT PRIMARY KEY,
  concert_id int DEFAULT NULL,
  FOREIGN KEY (concert_id) REFERENCES concert(concert_id),
  price float DEFAULT NULL
);

DROP TABLE IF EXISTS user_ticket;
CREATE TABLE user_ticket (
	user_id int DEFAULT NULL,
	FOREIGN KEY (user_id) REFERENCES tf_user(user_id),
    ticket_id int DEFAULT NULL,
	FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id)
);
