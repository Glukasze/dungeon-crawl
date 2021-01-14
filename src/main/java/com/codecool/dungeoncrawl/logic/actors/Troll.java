package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Troll extends Actor {
    public Troll(Cell cell) {
        super(cell);
        setTrollHealth();
    }

    @Override
    public String getTileName() {
        return "troll";
    }

    private void setTrollHealth() {
        this.addHealth(10);
    }
}
