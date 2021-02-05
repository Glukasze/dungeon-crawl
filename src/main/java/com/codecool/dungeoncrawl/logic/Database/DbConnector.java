package com.codecool.dungeoncrawl.logic.Database;

import java.sql.*;

public class DbConnector {

    private static String URL = "jdbc:postgresql://localhost/dungeon-crawl";
    private static String username = "postgres";
    private static String password = "Codecool";

    public static Connection connect() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL, username, password);
            System.out.println("DB active!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
