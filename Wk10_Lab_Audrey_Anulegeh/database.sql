
CREATE DATABASE IF NOT EXISTS employment_db;
USE employment_db;

CREATE TABLE employment_applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    current_address VARCHAR(255),
    contact_number VARCHAR(20),
    email VARCHAR(100),
    education VARCHAR(100),
    gender VARCHAR(20),
    date_available DATE,
    desired_position VARCHAR(100),
    desired_salary DECIMAL(10,2),
    authorized_to_work VARCHAR(5),
    relatives_working VARCHAR(5),
    relatives_explanation VARCHAR(255),
    applicant_signature VARCHAR(100)
);


USE employment_db;

SELECT * FROM employment_applications;