package ru.anastasya.readingportal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

    private static final String dbUsername = System.getenv("DB_USERNAME");
    private static final String dbPassword = System.getenv("DB_PASSWORD");
    private static final String dbURL;

    static {
        Properties properties = new Properties();

        try(InputStream inputStream = DBConnector.class.getClassLoader().getResourceAsStream("config.properties")) {

            properties.load(inputStream);
            dbURL = properties.getProperty("db.url");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("Кто то взял соединение");
            return DriverManager.getConnection(dbURL, dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось подключиться к базе данных", e);
        }
    }

}
