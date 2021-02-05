package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.Database.DbExecutor;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GameLoad {

    private String playerName;
    private int playerID;
    private int playerHealth;
    private ArrayList<String> playerInventory = new ArrayList<>();
    private String currentMap;
    private int playerX;
    private int playerY;
    private LocalDateTime savedAt;

    DbExecutor executor = new DbExecutor();

    public GameLoad(String playerName) {
        this.playerName = playerName;
    }

    public GameLoad(String playerName, int start) {

        this.playerName = playerName;
        try {
            this.playerID = executor.getIntByColumn("SELECT id FROM player WHERE player_name = '"+playerName+"'",
                    "id");
            this.playerHealth = executor.getIntCellByColumnInPlayer("hp", "player_name",
                    "Rozan");
            this.playerInventory = executor.getInventory(playerID);
            this.currentMap = executor.getMap(playerID);
            this.playerX = executor.getX(playerID);
            this.playerY = executor.getY(playerID);
            this.savedAt = executor.getDateCellByColumnInGameState("saved_at", "player_id",
                    playerID);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(playerHealth);
        System.out.println(playerInventory);
        System.out.println(playerName);
        System.out.println(currentMap);
        System.out.println(playerX);
        System.out.println(playerY);
        System.out.println(savedAt);
    }

    public int hetHealth() {
        return this.playerHealth;
    }

    public ArrayList<String> getInventory() {
        return this.playerInventory;
    }

    public String getMap() {
        return this.currentMap;
    }

    public int getX() {
        return playerX;
    }

    public int getY() {
        return this.playerY;
    }

    public boolean checkIfAlreadySaved() throws SQLException {
        return executor.checkStringInColumn("SELECT * FROM player", playerName, "player_name");
    }

}
