package com.novatech.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties properties = new Properties();

    static {
        try {

            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }

            properties.load(inputStream);
            inputStream.close();


            String driverClass = properties.getProperty("jdbc.driver");
            if (driverClass == null) {
                throw new RuntimeException("jdbc.driver property not found in " + PROPERTIES_FILE);
            }

            Class.forName(driverClass);
        } catch (ClassNotFoundException | IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        if (url == null || username == null || password == null) {
            throw new SQLException("Database connection properties not found");
        }

        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}