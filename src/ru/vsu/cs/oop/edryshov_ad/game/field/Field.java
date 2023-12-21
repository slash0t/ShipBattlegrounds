package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.Vector2;

import java.util.*;

public class Field {
    private final Cell startCell;

    private final Map<Ship, ArrayList<Water>> shipMap;

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

    public Map<Ship, ArrayList<Water>> getShipMap() {
        return shipMap;
    }

    public ArrayList<Water> getShipCells(Ship ship) {
        return shipMap.get(ship);
    }

    public Water getShipHead(Ship ship) {
        return shipMap.get(ship).get(0);
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
        ArrayList<Water> cells = shipMap.getOrDefault(ship, new ArrayList<>());
        cells.add(water);
        water.setShip(ship);
        shipMap.put(ship, cells);
    }

    public Water moveShipTo(Ship ship, Cell startCell, Vector2 direction) throws FieldException {
        LinkedList<Water> newPositions = new LinkedList<>();
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
        return newPositions.getFirst();
    }

    public SailingResult getSailingResultInRect(
            Ship ship, Cell startCell,
            int size, Vector2 horizontalDir, Vector2 verticalDir
    ) {
        SailingResult result = SailingResult.SAILED;
        Cell anchorCell = startCell;
        for (int i = 1; i < size && result == SailingResult.SAILED; i++) {
            anchorCell = anchorCell.getFromDirection(horizontalDir);

            Cell curr = anchorCell;

            for (int j = 0; j < size && result == SailingResult.SAILED; j++) {
                result = curr.canShipSailThrough(ship);
                curr = curr.getFromDirection(verticalDir);
            }
        }

        return result;
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
