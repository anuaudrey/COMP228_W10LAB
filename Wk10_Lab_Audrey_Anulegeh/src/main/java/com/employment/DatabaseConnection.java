package com.employment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Description:
 * This class connects the Employment Application
 * to the MySQL database using JDBC.
 *
 * It uses DriverManager.getConnection() to establish
 * a connection to the employment_db database.
 */
public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/employment_db";

    private static final String USER =
            System.getenv("EMPLOYMENT_DB_USER");

    private static final String PASSWORD =
            System.getenv("EMPLOYMENT_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

