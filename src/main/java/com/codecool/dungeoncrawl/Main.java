package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Bug;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Main extends Application {


    GameMap map = MapLoader.loadMap("/1.txt");
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();

    int currentX = map.getPlayer().getX();
    int currentY = map.getPlayer().getY();
    private Player player = map.getPlayer();
    private String currentMap = "/1.txt";

    public Main() {
    }


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

    private void saveGame() {

        GameSave save = new GameSave(player.getName(), 1, player.getHealth(), player.getInventoryAsStringList(),
                currentMap, player.getX(), player.getY());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Saving");
        alert.setHeaderText("Do you want to save game?");
        alert.setContentText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                if (save.checkIfAlreadySaved()) {
                    overwrite(save);
                } else {
                    save.save();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void overwrite(GameSave save) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save already exists!");
        alert.setHeaderText("Do you want to overwrite your save?");
        alert.setContentText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            save.overwrite();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private ArrayList<Item> setupInventory(ArrayList<String> loadedInventory) {
        ArrayList<Item> result = new ArrayList<>();
        for (String loadedItem : loadedInventory) {
            if (loadedItem.equals("key")) {
                Item item = new Key(player.getCell());
                result.add(item);
            } else if (loadedItem.equals("sword")) {
                Item item = new Sword(player.getCell());
                result.add(item);
            }
        }
        return result;
    }

    private void noSave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nothing to load!");
        alert.setHeaderText("Save file missing");
        alert.setContentText(null);
        alert.showAndWait();
    }

    private void loadGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Load saved game");
        alert.setHeaderText("Do you want to load saved game?");
        alert.setContentText(null);

        GameLoad preLoad = new GameLoad(player.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                if (preLoad.checkIfAlreadySaved()) {
                    GameLoad load = new GameLoad(player.getName(), 1);
                    this.player.setInventory(setupInventory(load.getInventory()));
                    newLevel(load.getMap(), this.player.getInventory(), load.hetHealth());
                } else {
                    noSave();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
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
                break;
            case S:
                saveGame();
                break;
            case L:
                loadGame();;
                break;

        }
        refresh();
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

    private void newLevel(String mapName, ArrayList<Item> inventory, int health) {
        map = MapLoader.loadMap(mapName);
        this.currentMap = mapName;
        map.getPlayer().setInventory(inventory);
        map.getPlayer().updateDamage();
        map.getPlayer().setHealth(health);
        this.player = map.getPlayer();
    }

    private void playerMove(int[] direction) {

        if (!map.getCell(currentX + direction[0], currentY + direction[1]).getTileName().equals("wall") &&
                map.getCell(currentX + direction[0], currentY + direction[1]).getActor() == null &&
                doorCheck(direction)) {
            map.getPlayer().move(direction[0], direction[1]);
            if (map.getCell(currentX + direction[0], currentY + direction[1]).getTileName().equals("entrance")) {
                newLevel("/2.txt", this.player.getInventory(), player.getHealth());
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
