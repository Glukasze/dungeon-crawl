package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.Database.DbConnector;
import com.codecool.dungeoncrawl.logic.Database.DbExecutor;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class GameSave {

    private String playerName;
    private int playerID;
    private int playerHealth;
    private ArrayList<String> playerInventory = new ArrayList<>();
    private String currentMap;
    private int playerX;
    private int playerY;
    private LocalDateTime savedAt;

    DbExecutor executor = new DbExecutor();

    public GameSave(String playerName, int playerID, int playerHealth, ArrayList<String> playerInventory,
                    String currentMap, int playerX, int playerY) {

        this.playerName = playerName;
        this.playerID = playerID;
        this.playerHealth = playerHealth;
        this.playerInventory = playerInventory;
        this.currentMap = currentMap;
        this.playerX = playerX;
        this.playerY = playerY;
        this.savedAt = LocalDateTime.now();
    }

    public void save() {
        PreparedStatement st = null;
        DbExecutor.execute("INSERT INTO player(player_name, hp, x, y)" +
                "VALUES('"+playerName+"', "+playerHealth+", "+playerX+", "+playerY+")");

        for (String item : playerInventory) {
            try {
                st = DbConnector.connect().prepareStatement("INSERT INTO " +
                        "inventory (item, player_id) VALUES (?,?)");
                st.setObject(1, item);
                st.setObject(2, playerID);
                st.executeUpdate(); st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        try {
            st = DbConnector.connect().prepareStatement("INSERT INTO " +
                    "game_state (current_map, saved_at, player_id) VALUES (?,?,?)");
            st.setObject(1, currentMap);
            st.setObject(2, savedAt);
            st.setObject(3, playerID);
            st.executeUpdate(); st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void overwrite() {

        DbExecutor.execute("UPDATE player SET hp = "+playerHealth+"," +
                " x = "+playerX+", y = "+playerY+" " +
                "WHERE player_name = '"+playerName+"'");

        PreparedStatement st = null;

        try {
            st = DbConnector.connect().prepareStatement("DELETE FROM " +
                    "inventory WHERE player_id = ?");
            st.setObject(1, playerID);
            st.executeUpdate(); st.close();

            for (String item : playerInventory) {
                st = DbConnector.connect().prepareStatement("INSERT INTO " +
                        "inventory (item, player_id) VALUES (?,?)");
                st.setObject(1, item);
                st.setObject(2, playerID);
                st.executeUpdate();
                st.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            st = DbConnector.connect().prepareStatement("UPDATE game_state SET " +
                    "current_map = ?, saved_at = ? WHERE player_id = ?");
            st.setObject(1, currentMap);
            st.setObject(2, savedAt);
            st.setObject(3, playerID);
            st.executeUpdate(); st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkIfAlreadySaved() throws SQLException {
        return executor.checkStringInColumn("SELECT * FROM player", playerName, "player_name");
    }

}
