package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.Database.DbExecutor;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class GameSave {

    private String playerName;
    private int playerID;
    private int playerHealth;
    private ArrayList<String> playerInvewntory = new ArrayList<>();
    private String currentMap;
    private int playerX;
    private int playerY;
    private Date savedAt;

    DbExecutor executor = new DbExecutor();

    public GameSave(String playerName, int playerID, int playerHealth, ArrayList<String> playerInventory,
                    String currentMap, int playerX, int playerY) {

        this.playerName = playerName;
        this.playerID = playerID;
        this.playerHealth = playerHealth;
        this.playerInvewntory = playerInventory;
        this.currentMap = currentMap;
        this.playerX = playerX;
        this.playerY = playerY;
//        this.savedAt = new java.util.Date();
    }

    public void save() {
        DbExecutor.execute("INSERT INTO player(player_name, hp, x, y)" +
                "VALUES('"+playerName+"', "+playerHealth+", "+playerX+", "+playerY+")");
        System.out.println("SAVED");
    }

    public void overwrite() {

        DbExecutor.execute("UPDATE player SET hp = "+playerHealth+", x = "+playerX+", y = "+playerY+" " +
                "WHERE player_name = '"+playerName+"'");
        System.out.println("OVERWRITTEN");
    }

    public boolean checkIfAlreadySaved() throws SQLException {
        return executor.checkStringInColumn("SELECT * FROM player", playerName, "player_name");
    }

}
