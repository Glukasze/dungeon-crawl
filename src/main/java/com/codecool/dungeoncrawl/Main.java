package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
                if (!map.getCell(currentX, currentY - 1).getTileName().equals("wall") & map.getCell(currentX, currentY - 1).getActor() == null) {
                    map.getPlayer().move(0, -1);
                }
                refresh();
                break;
            case DOWN:
                if (!map.getCell(currentX, currentY + 1).getTileName().equals("wall") & map.getCell(currentX, currentY + 1).getActor() == null) {
                    map.getPlayer().move(0, 1);
                }
                refresh();
                break;
            case LEFT:
                if (!map.getCell(currentX - 1, currentY).getTileName().equals("wall") && map.getCell(currentX - 1, currentY).getActor() == null) {
                    map.getPlayer().move(-1, 0);
                }
                refresh();
                break;
            case RIGHT:
                if (!map.getCell(currentX + 1, currentY).getTileName().equals("wall") && map.getCell(currentX + 1, currentY).getActor() == null) {
                    map.getPlayer().move(1, 0);
                }
                refresh();
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
    }
}
