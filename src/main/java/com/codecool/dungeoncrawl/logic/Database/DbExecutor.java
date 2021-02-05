package com.codecool.dungeoncrawl.logic.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbExecutor {

    public static ResultSet select(String query) {

        try {
            Connection conn = DbConnector.connect();
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void execute(String query) {

        try {
            Connection conn = DbConnector.connect();
            Statement statement = conn.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean checkStringInColumn(String query, String key, String column) throws SQLException {
        ResultSet resultSet = select(query);
        while (resultSet.next()) {
            if (resultSet.getString(column).equals(key)) {
                return true;
            }
        }
        return false;
    }

    public int getIntByColumn(String query, String column) throws SQLException {
        ResultSet resultSet = select(query);
        resultSet.next();
        return resultSet.getInt(column);
    }

}
