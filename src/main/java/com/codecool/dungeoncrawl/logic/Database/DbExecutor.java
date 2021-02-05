package com.codecool.dungeoncrawl.logic.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DbExecutor {

    public static ResultSet select(String query) {

        try {
            Connection conn = DbConnector.connect();
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
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

    public ArrayList<String> getInventory(int playerID) {
        ArrayList<String> result = new ArrayList<>();
        ResultSet resultSet = select("SELECT * FROM inventory WHERE player_id = "+playerID+"");
        try {
            while (resultSet.next()) {
                result.add(resultSet.getString("item"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public String getMap(int playerID) {
        String result = null;
        ResultSet resultSet = select("SELECT current_map FROM game_state WHERE player_id = "+playerID+"");
        try {
            resultSet.next();
            result = resultSet.getString("current_map");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public int getX(int playerID) {
        int result = 0;
        ResultSet resultSet = select("SELECT x FROM player WHERE id = "+playerID+"");
        try {
            resultSet.next();
            result = resultSet.getInt("x");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public int getY(int playerID) {
        int result = 0;
        ResultSet resultSet = select("SELECT y FROM player WHERE id = "+playerID+"");
        try {
            resultSet.next();
            result = resultSet.getInt("y");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
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

    public ResultSet getTablePlayer() throws SQLException {
        PreparedStatement st = null;
        ResultSet result = null;
        try {
            st = DbConnector.connect().prepareStatement("SELECT * FROM player");
            result = st.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public ResultSet getTableGameState() throws SQLException {
        PreparedStatement st = null;
        ResultSet result = null;
        try {
            st = DbConnector.connect().prepareStatement("SELECT * FROM game_state");
            result = st.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public int getIntCellByColumnInPlayer(String column, String keyColumn, String key)
            throws SQLException {
        int result = 0;
        ResultSet resultSet = getTablePlayer();

        while (resultSet.next()) {
            if (resultSet.getString(keyColumn).equals(key)) {
                result = resultSet.getInt(column);
            }
        }
        return result;
    }

    public String getStringCellByColumnInPlayer(String column, String keyColumn, String key)
            throws SQLException {
        String result = null;
        ResultSet resultSet = getTablePlayer();

        while (resultSet.next()) {
            if (resultSet.getString(keyColumn).equals(key)) {
                result = resultSet.getString(column);
            }
        }
        return result;
    }


    public String getStringCellByColumnInGameState(String column, String keyColumn, int key)
            throws SQLException {
        String result = null;
        ResultSet resultSet = getTableGameState();
        while (resultSet.next()) {
            if (resultSet.getInt(keyColumn) == (key)) {
                result = resultSet.getString(column);
            }
        }
        return result;
    }

    public LocalDateTime getDateCellByColumnInGameState(String column, String keyColumn, int key)
            throws SQLException {
        LocalDateTime result = null;
        ResultSet resultSet = getTableGameState();
        while (resultSet.next()) {
            if (resultSet.getInt(keyColumn) == (key)) {
                result = resultSet.getObject(column, LocalDateTime.class);
            }
        }
        return result;
    }
}
