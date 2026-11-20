
package com.employment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Description:
 * This JavaFX application displays an employment application form.
 * Users enter their personal and employment information and click
 * Submit to save the information in a MySQL database.
 *
 * JDBC PreparedStatement is used to insert the information securely.
 */
public class App extends Application {

    // Personal information fields
    private TextField nameField = new TextField();
    private TextField addressField = new TextField();
    private TextField phoneField = new TextField();
    private TextField emailField = new TextField();
    private TextField educationField = new TextField();

    private ComboBox<String> genderBox = new ComboBox<>();

    // Employment information fields
    private DatePicker availableDate = new DatePicker();
    private TextField positionField = new TextField();
    private TextField salaryField = new TextField();

    private ComboBox<String> authorizedBox = new ComboBox<>();
    private ComboBox<String> relativesBox = new ComboBox<>();

    private TextField explanationField = new TextField();
    private TextField signatureField = new TextField();

    private Label statusLabel = new Label();

    @Override
    public void start(Stage stage) {

        // Main form layout
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        // Application title
        Label title = new Label("Employment Application");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // ComboBox options
        genderBox.getItems().addAll("Male", "Female", "Other");
        authorizedBox.getItems().addAll("Yes", "No");
        relativesBox.getItems().addAll("Yes", "No");

        // Add form controls
        form.getChildren().addAll(
                title,

                new Label("Personal Information"),

                new Label("Full Name"), nameField,
                new Label("Current Address"), addressField,
                new Label("Contact Number"), phoneField,
                new Label("Email Address"), emailField,
                new Label("Highest Educational Attainment"), educationField,
                new Label("Gender"), genderBox,

                new Label("Employment Information"),

                new Label("Date Available"), availableDate,
                new Label("Desired Position"), positionField,
                new Label("Desired Salary"), salaryField,

                new Label("Legally Authorized to Work?"), authorizedBox,
                new Label("Relatives Working for the Company?"), relativesBox,
                new Label("If Yes, Explain"), explanationField,

                new Label("Applicant Signature (Type Full Name)"),
                signatureField
        );

        // Submit button
        Button submitButton = new Button("Submit Application");

        // Call the submit method when the button is clicked
        submitButton.setOnAction(e -> submitApplication());

        form.getChildren().addAll(submitButton, statusLabel);

        // Allow scrolling through the form
        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 550, 650);

        stage.setTitle("Employment Application Form");
        stage.setScene(scene);
        stage.show();
    }

    /*
     * Description:
     * Validates the form and inserts the application information
     * into MySQL using a PreparedStatement.
     */
    private void submitApplication() {

        // Validate required fields
        if (nameField.getText().trim().isEmpty()
                || signatureField.getText().trim().isEmpty()) {

            statusLabel.setText("Please enter your name and signature.");
            return;
        }

        // Validate desired salary
        BigDecimal salary;

        try {
            salary = new BigDecimal(salaryField.getText().trim());

            if (salary.signum() < 0) {
                statusLabel.setText("Salary cannot be negative.");
                return;
            }

        } catch (NumberFormatException ex) {

            statusLabel.setText("Please enter a valid salary.");
            return;
        }

        // SQL INSERT statement with placeholders
        String sql = """
                INSERT INTO employment_applications
                (full_name, current_address, contact_number,
                 email, education, gender, date_available,
                 desired_position, desired_salary, authorized_to_work,
                 relatives_working, relatives_explanation,
                 applicant_signature)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        // Connect to MySQL and prepare the SQL statement
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the values for the placeholders
            stmt.setString(1, nameField.getText().trim());
            stmt.setString(2, addressField.getText().trim());
            stmt.setString(3, phoneField.getText().trim());
            stmt.setString(4, emailField.getText().trim());
            stmt.setString(5, educationField.getText().trim());
            stmt.setString(6, genderBox.getValue());

            // Admission/availability date
            if (availableDate.getValue() != null) {
                stmt.setDate(7, Date.valueOf(availableDate.getValue()));
            } else {
                stmt.setNull(7, java.sql.Types.DATE);
            }

            stmt.setString(8, positionField.getText().trim());
            stmt.setBigDecimal(9, salary);
            stmt.setString(10, authorizedBox.getValue());
            stmt.setString(11, relativesBox.getValue());
            stmt.setString(12, explanationField.getText().trim());
            stmt.setString(13, signatureField.getText().trim());

            // Execute the INSERT operation
            stmt.executeUpdate();

            // Display success message
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Application submitted successfully!");

        } catch (SQLException ex) {

            // Display database error
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Database error: " + ex.getMessage());
        }
    }

    // Launch JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
