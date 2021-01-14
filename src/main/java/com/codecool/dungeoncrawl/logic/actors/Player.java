package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;

public class Player extends Actor {

    private ArrayList<Item> inventory = new ArrayList<Item>();

    public Player(Cell cell) {
        super(cell);
    }

    public void addToInventory(Item item) {
        this.inventory.add(item);
        if (item.getItemType().equals("weapon")) {
            this.increaseDamage(item.getDamage());
        }
    }

    public void removeFromInventory(Item item) {
        this.inventory.remove(item);
        if (item.getItemType().equals("weapon")) {
            this.decreaseDamage(item.getDamage());
        }
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    public String getInventoryAsString() {
        String result = "";
        for (Item item : inventory) {
            result += (item.getTileName() + "\n");
        }
        return result;
    }

    public String getTileName() {
        return "player";
    }
}
