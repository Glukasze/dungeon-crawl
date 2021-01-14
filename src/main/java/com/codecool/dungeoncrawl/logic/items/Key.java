package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Key extends Item {

    private String itemType = "key";

    public Key(Cell cell) {
        super(cell);
    }

    @Override
    public int getDamage() {
        return 0;
    }

    public String getTileName() {
        return "key";
    }

    @Override
    public String getItemType() {
        return itemType;
    }
}