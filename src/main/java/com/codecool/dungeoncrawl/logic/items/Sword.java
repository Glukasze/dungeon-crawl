package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Item {
    private String itemType = "weapon";
    private int plusDamage = 5;

    public Sword(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "sword";
    }

    public int getDamage() {
        return plusDamage;
    }

    @Override
    public String getItemType() {
        return itemType;
    }
}
