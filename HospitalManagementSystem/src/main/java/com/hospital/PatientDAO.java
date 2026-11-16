package com.hospital;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Description:
 * The PatientDAO class handles database operations for patient records.
 * It uses JDBC to connect to MySQL and perform Create, Read, Update,
 * Delete, and Search operations.
 *
 * PreparedStatement is used to execute SQL queries safely.
 * Try-with-resources automatically closes database resources.
 */

public class PatientDAO {

    // CREATE: Adds a new patient to the database.
    public void addPatient(Patient patient) throws SQLException {

        String sql = "INSERT INTO patients (name, age, gender, phone, diagnosis, admission_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the values for the SQL placeholders.
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getDiagnosis());
            stmt.setDate(6, Date.valueOf(patient.getAdmissionDate()));

            // Execute the INSERT statement.
            stmt.executeUpdate();
        }
    }

    // READ: Retrieves all patients from the database.
    public List<Patient> getAllPatients() throws SQLException {

        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patients ORDER BY patient_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("diagnosis"),
                    rs.getDate("admission_date").toLocalDate()
                );

                patients.add(patient);
            }
        }

        return patients;
    }

    // UPDATE: Updates an existing patient's information.
    public void updatePatient(Patient patient) throws SQLException {

        String sql = "UPDATE patients SET name = ?, age = ?, gender = ?, phone = ?, diagnosis = ?, admission_date = ? WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getDiagnosis());
            stmt.setDate(6, Date.valueOf(patient.getAdmissionDate()));

            stmt.setInt(7, patient.getId());

            stmt.executeUpdate();
        }
    }

    // DELETE: Removes a patient using their ID.
    public void deletePatient(int patientId) throws SQLException {

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

       
            stmt.setInt(1, patientId);

            
            stmt.executeUpdate();
        }
    }

    // SEARCH: Finds patients by name or phone number.
    public List<Patient> searchPatients(String keyword) throws SQLException {

        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patients WHERE name LIKE ? OR phone LIKE ? ORDER BY patient_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("diagnosis"),
                        rs.getDate("admission_date").toLocalDate()
                    );

                    patients.add(patient);
                }
            }
        }

        return patients;
    }
}
