package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;

public class Player extends Actor {

    private ArrayList<Item> inventory = new ArrayList<Item>();
    private String name = "Rozan";

    public Player(Cell cell) {
        super(cell);
    }

    public String getName() {
        return this.name;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public void updateDamage() {
        this.setDamage(5);
        for (Item item : inventory) {
            if (item.getItemType().equals("weapon")) {
                this.increaseDamage(item.getDamage());
            }
        }
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

    public ArrayList<String> getInventoryAsStringList() {
        ArrayList<String> result = new ArrayList<>();
        for (Item item : inventory) {
            result.add(item.getTileName());
        }
        if (result.size() < 1) {
            result.add("empty");
        }
        System.out.println(result);
        return result;
    }

    public boolean inventoryContainsItem(String itemType) {
        for (Item item:inventory) {
            if (item.getItemType().equals(itemType)) {
                return true;
            }
        } return false;
    }

    public String getTileName() {
        return "player";
    }
}
