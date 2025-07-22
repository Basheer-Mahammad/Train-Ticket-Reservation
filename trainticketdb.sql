CREATE DATABASE IF NOT EXISTS trainticketdb;
USE trainticketdb;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(50),
    lname VARCHAR(50),
    mailid VARCHAR(100) UNIQUE,
    pword VARCHAR(100),
    address VARCHAR(255),
    role VARCHAR(20)
);

-- Trains table
CREATE TABLE IF NOT EXISTS trains (
    tr_no BIGINT PRIMARY KEY,
    tr_name VARCHAR(100),
    from_stn VARCHAR(100),
    to_stn VARCHAR(100),
    seats INT,
    fare DECIMAL(10,2)
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    train_no BIGINT,
    journey_date DATE,
    seat_class VARCHAR(20),
    seats INT,
    status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (train_no) REFERENCES trains(tr_no)
);

-- Insert default admin and user
INSERT INTO users (fname, lname, mailid, pword, address, role) VALUES
('Admin', 'User', 'admin@demo.com', 'admin', 'Admin Address', 'ADMIN'),
('Shashi', 'User', 'shashi@demo.com', 'shashi', 'User Address', 'CUSTOMER');
