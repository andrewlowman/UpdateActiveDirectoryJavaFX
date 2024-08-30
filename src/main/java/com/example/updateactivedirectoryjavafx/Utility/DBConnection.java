package com.example.updateactivedirectoryjavafx.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/schema_db";
    private static Connection conn = null;
    private static final String username = "root";
    private static final String password = "Monsterenergy223";

    public static Connection startConnection(){
        try{
            conn = DriverManager.getConnection(jdbcURL,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    public static void closeConnection(){
        try{
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
