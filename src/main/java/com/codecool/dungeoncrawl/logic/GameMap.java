package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Bug;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.items.Key;

import java.util.ArrayList;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;
    private Sword sword;
    private Key key;
    private ArrayList<Bug> bugs = new ArrayList<Bug>();

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Bug> getBugs() {
        return bugs;
    }

    public void setBug(Bug bug) {
        this.bugs.add(bug);
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public Sword getSword() {
        return sword;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
