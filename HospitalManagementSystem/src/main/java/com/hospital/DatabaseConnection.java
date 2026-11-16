
package com.hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = System.getenv().getOrDefault("HOSPITAL_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("HOSPITAL_DB_PASSWORD", "");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
