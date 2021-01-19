package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Bug;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Troll;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/1.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case 'w':
                            cell.setType(CellType.WATER);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 'x':
                            cell.setType(CellType.ENTRANCE);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case 't':
                            cell.setType(CellType.FLOOR);
                            new Troll(cell);
                            break;
                        case 'b':
                            cell.setType(CellType.FLOOR);
                            map.setBug(new Bug(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case '1':
                            cell.setType(CellType.FLOOR);
                            map.setSword(new Sword(cell));
                            break;
                        case '2':
                            cell.setType(CellType.FLOOR);
                            map.setKey(new Key(cell));
                            break;
                        case 'c':
                            cell.setType(CellType.CLOSED);
                            break;
                        case 'o':
                            cell.setType(CellType.OPEN);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
