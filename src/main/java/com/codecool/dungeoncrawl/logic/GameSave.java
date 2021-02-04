package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.Database.DbExecutor;
import com.codecool.dungeoncrawl.logic.items.Item;

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

//    public void export() {
//        DbExecutor.execute("INSERT INTO game_state(current_map, saved_at, player_id)" +
//                "VALUES('"+currentMap+"','"+savedAt+"', '"+playerID+"')");
//    }

    public void export() {
        DbExecutor.execute("INSERT INTO player(player_name, hp, x, y)" +
                "VALUES('"+playerName+"', "+playerHealth+", "+playerX+", "+playerY+")");
    }

}
