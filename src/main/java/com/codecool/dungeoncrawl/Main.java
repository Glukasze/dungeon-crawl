package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Bug;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();

    int currentX = map.getPlayer().getX();
    int currentY = map.getPlayer().getY();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(new Label("Inventory:"), 0,1);
        ui.add(inventoryLabel, 0,2);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {



        switch (keyEvent.getCode()) {
            case UP:
                playerMove(generateDirection("up"));
                break;
            case DOWN:
                playerMove(generateDirection("down"));
                break;
            case LEFT:
                playerMove(generateDirection("left"));
                break;
            case RIGHT:
                playerMove(generateDirection("right"));
                break;
            case X:
                if (map.getCell(currentX, currentY).getItem() != null) {
                    map.getPlayer().addToInventory(map.getCell(currentX,currentY).getItem());
                    map.getCell(currentX, currentY).setItem(null);
                }
                refresh();
        }
    }


    private void refresh() {
        this.currentX = map.getPlayer().getX();
        this.currentY = map.getPlayer().getY();

        bugMove(generateDirection(randomDirection()), map.getBugs());
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        inventoryLabel.setText(map.getPlayer().getInventoryAsString());
        if (map.getPlayer().getHealth() <= 0) {
            map.setPlayer(null);
        }
    }

    private boolean doorCheck(int[] direction) {
        if (map.getCell(currentX + direction[0], currentY + direction[1]).getTileName().equals("closed")) {

            if (map.getPlayer().inventoryContainsItem("key")) {
                map.getCell(currentX + direction[0], currentY + direction[1]).setType(CellType.OPEN);
            } else {
                return false;
            }
        }
        return true;
    }

    private void playerMove(int[] direction) {

        if (!map.getCell(currentX + direction[0], currentY + direction[1]).getTileName().equals("wall") &&
                map.getCell(currentX + direction[0], currentY + direction[1]).getActor() == null &&
                doorCheck(direction)) {
            map.getPlayer().move(direction[0], direction[1]);
            if (map.getCell(currentX + direction[0], currentY + direction[1]).getTileName().equals("entrance")) {

            }
        } else if (map.getCell(currentX + direction[0], currentY + direction[1]).getActor() != null) {
            map.getCell(currentX + direction[0],currentY + direction[1]).getActor().subtractHealth(map.getPlayer().getDamage());
            if (map.getCell(currentX + direction[0],currentY + direction[1]).getActor().getHealth() <= 0) {
                map.getCell(currentX + direction[0],currentY + direction[1]).setActor(null);
            } else {
                map.getPlayer().subtractHealth(2);
            }
        }
        refresh();
    }

    private void bugMove(int[] direction, ArrayList<Bug> bugs) {
        int moves = 0;
        while (moves < 1) {
            for (Bug bug:bugs) {
                if (!map.getCell(bug.getX() + direction[0], bug.getY() + direction[1]).getTileName().equals("wall") &&
                        map.getCell(bug.getX() + direction[0], bug.getY() + direction[1]).getActor() == null &&
                        map.getCell(bug.getX() + direction[0], bug.getY() + direction[1]) != map.getCell(currentX, currentY)) {
                    bug.move(direction[0], direction[1]);
                    moves += 1;
                } else if (map.getCell(bug.getX() + direction[0], bug.getY() + direction[1]) == map.getCell(currentX, currentY)) {
                    map.getPlayer().subtractHealth(bug.getDamage());
                    moves += 1;
                } else {
                    direction = generateDirection(randomDirection());
                }
            }
        }
    }

    private int[] generateDirection(String directionName) {
        int[] result = new int[0];
        switch (directionName) {
            case "up":
                result = new int[]{0, -1};
                break;
            case "down":
                result = new int[]{0, 1};
                break;
            case "left":
                result = new int[]{-1, 0};
                break;
            case "right":
                result = new int[]{1, 0};
                break;
            }
            return result;
        }

    private String randomDirection() {
        Random rand = new Random();
        int selection = rand.nextInt(4);

        switch (selection) {
            case 0:
                return "up";
            case 1:
                return "down";
            case 2:
                return "left";
            case 3:
                return "right";
        }
        return "up";
    }
}
