package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Bug extends Actor {
    public Bug(Cell cell) {
        super(cell);
        setBugHealth();
    }

    @Override
    public String getTileName() {
        return "bug";
    }

    private void setBugHealth() {
        this.addHealth(5);
    }

    private void setBugDamage() {
        this.increaseDamage(5);
    }
}
