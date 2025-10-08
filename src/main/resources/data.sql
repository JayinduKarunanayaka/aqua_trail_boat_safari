-- Staff login credentials for role-based authentication
-- This will be automatically loaded by Spring Boot into your MySQL database

-- Create staff table if not exists
CREATE TABLE IF NOT EXISTS staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Clear existing test data to avoid conflicts
DELETE FROM staff WHERE email IN (
    'admin@company.com', 
    'manager@company.com', 
    'booking@company.com', 
    'marketing@company.com', 
    'captain@company.com'
);

-- Insert test staff data for login
INSERT INTO staff (name, email, role, password) VALUES 
('John Admin', 'admin@company.com', 'admin', 'admin123'),
('Jayindu Karunanayaka', 'manager@company.com', 'manager', 'manager123'),
('Dileka Samaranayaka', 'booking@company.com', 'booking', 'booking123'),
('Deepana Gunasinghe', 'marketing@company.com', 'marketing', 'marketing123'),
('Tom Captain', 'captain@company.com', 'captain', 'captain123');

-- Drop and recreate trip table to ensure clean schema
DROP TABLE IF EXISTS trip;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS issues;

-- Create trip table with specified attributes
CREATE TABLE trip (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_name VARCHAR(255) NOT NULL,
    description TEXT,
    duration VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    image_url VARCHAR(500),
    captain_id BIGINT,
    captain_name VARCHAR(255),
    boat_name VARCHAR(255)
);

-- Create booking table
CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    trip_id BIGINT NOT NULL,
    trip_name VARCHAR(255) NOT NULL,
    booking_date DATETIME NOT NULL,
    status VARCHAR(50) DEFAULT 'confirmed',
    total_price DECIMAL(10,2) NOT NULL
);

-- Create feedback table
CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    feedback_text TEXT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    feedback_date DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'active'
);

-- Create reports table
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_date DATETIME NOT NULL,
    report_type VARCHAR(50) NOT NULL
);

-- Create issues table
CREATE TABLE issues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    reported_by VARCHAR(255) NOT NULL,
    captain_email VARCHAR(255),
    created_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'open',
    priority VARCHAR(20) NOT NULL DEFAULT 'medium'
);

-- Insert Sri Lankan boat trip data
INSERT INTO trip (trip_name, description, duration, price, status, image_url, captain_id, captain_name, boat_name) VALUES  
('Kaluganga Tour', 'Enjoy a scenic boat ride along the Kaluganga river with lush surroundings.', '02:30:00', 4500.00, 'active', 'https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=500&h=300&fit=crop', 5, 'Tom Captain', 'River Explorer'),
('Mahaweli Tour', 'Discover the longest river in Sri Lanka with a peaceful boat journey.', '03:00:00', 6000.00, 'active', 'https://images.unsplash.com/photo-1506197603052-3cc9c3a201bd?w=500&h=300&fit=crop', 5, 'Tom Captain', 'Aqua Adventure'),
('Madu River Tour', 'A nature-filled tour through Madu River, rich with mangroves and small islands.', '02:00:00', 5000.00, 'active', 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=500&h=300&fit=crop', NULL, NULL, NULL),
('Negombo Lagoon Tour', 'Relax with a lagoon cruise in Negombo, full of fishing culture and birdlife.', '01:30:00', 3500.00, 'active', 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500&h=300&fit=crop', NULL, NULL, NULL),
('Bentota River Safari', 'Experience wildlife and river life with a fun boat safari in Bentota.', '02:15:00', 4000.00, 'active', 'https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=500&h=300&fit=crop', 5, 'Tom Captain', 'Safari Queen'),
('Kalu River Adventure', 'An adventurous ride along the scenic Kalu River surrounded by greenery.', '02:45:00', 4800.00, 'active', 'https://images.unsplash.com/photo-1449824913935-59a10b8d2000?w=500&h=300&fit=crop', NULL, NULL, NULL),
('Nilwala River Cruise', 'Take a calm journey along Nilwala River near Matara with crocodile spotting.', '02:30:00', 5200.00, 'active', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=500&h=300&fit=crop', NULL, NULL, NULL),
('Trincomalee Harbour Tour', 'Explore the world-famous natural harbour with a beautiful sea ride.', '01:45:00', 5500.00, 'active', 'https://images.unsplash.com/photo-1439066615861-d1af74d74000?w=500&h=300&fit=crop', NULL, NULL, NULL),
('Batticaloa Lagoon Ride', 'A serene trip through Batticaloa Lagoon with mangroves and traditional life.', '02:00:00', 4200.00, 'active', 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500&h=300&fit=crop', NULL, NULL, NULL),
('Koggala Lake Safari', 'A popular trip in Galle through Koggala Lake with islands and temples.', '02:15:00', 4600.00, 'active', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=500&h=300&fit=crop', NULL, NULL, NULL);

-- Insert sample issues for testing
INSERT INTO issues (title, description, reported_by, captain_email, created_date, status, priority) VALUES
('Engine Making Strange Noise', 'The boat engine has been making unusual rattling sounds during the last two trips. It seems to occur mainly when accelerating.', 'Tom Captain', 'captain@company.com', NOW(), 'open', 'high'),
('Safety Equipment Missing', 'Found that some life jackets are missing from Boat #3. We need to restock before the next tour.', 'Tom Captain', 'captain@company.com', DATE_SUB(NOW(), INTERVAL 1 DAY), 'in_progress', 'medium');