package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.Vector2;

import java.util.*;

public class Field {
    private final Cell startCell;

    private Map<Ship, Set<Water>> shipMap;

    private final int width;
    private final int height;

    public Field(Cell startCell, int width, int height) {
        this.startCell = startCell;
        this.shipMap = new TreeMap<>();
        this.width = width;
        this.height = height;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public Map<Ship, Set<Water>> getShipMap() {
        return shipMap;
    }

    public Set<Water> getShipCells(Ship ship) {
        return shipMap.get(ship);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void deleteShip(Ship ship) {
        if (!shipMap.containsKey(ship)) {
            return;
        }

        for (Water water : shipMap.get(ship)) {
            water.removeShip();
        }

        shipMap.remove(ship);
    }

    private void addShipCoordinate(Ship ship, Water water) {
        Set<Water> cells = shipMap.getOrDefault(ship, new TreeSet<>());
        cells.add(water);
        water.setShip(ship);
        shipMap.put(ship, cells);
    }

    public ArrayList<Water> moveShipTo(Ship ship, Cell startCell, Vector2 direction) throws FieldException {
        ArrayList<Water> newPositions = new ArrayList<>(ship.getSize());
        Cell currCell = startCell;
        for (int i = 0; i < ship.getSize(); i++) {
            if (currCell.canShipSailThrough(ship) != SailingResult.SAILED) {
                throw new FieldException("Illegal ship placement");
            }
            if (currCell instanceof Water water) {
                newPositions.add(water);
            } else {
                throw new FieldException("Can't place ship on this type of cell");
            }

            currCell = currCell.getFromDirection(direction);
        }

        deleteShip(ship);
        for (Water water : newPositions) {
            addShipCoordinate(ship, water);
        }
        return newPositions;
    }

    @Override
    public String toString() {
        Cell anchor = startCell;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            Cell curr = anchor;
            for (int j = 0; j < width; j++) {
                if (curr instanceof Water water) {
                    Ship ship = water.getShip();
                    if (ship == null) {
                        sb.append("~");
                    } else {
                        if (ship.isSunk()) {
                            sb.append("X");
                        } else {
                            sb.append("S");
                        }
                    }
                } else {
                    sb.append("B");
                }
                curr = curr.getRight();
            }
            anchor = anchor.getBottom();
            sb.append("\n");
        }

        return sb.toString();
    }
}
