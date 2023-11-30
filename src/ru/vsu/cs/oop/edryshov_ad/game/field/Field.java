package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Field {
    private final Cell startCell;

    private Map<Ship, Set<Water>> shipMap;

    public Field(Cell startCell) {
        this.startCell = startCell;
        this.shipMap = new TreeMap<>();
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

    public void deleteShip(Ship ship) {
        for (Water water : shipMap.get(ship)) {
            water.setShip(null);
        }

        shipMap.remove(ship);
    }

    public void addShipCoordinate(Ship ship, Water water) {
        Set<Water> cells = shipMap.getOrDefault(ship, new TreeSet<>());
        cells.add(water);
        shipMap.put(ship, cells);
    }
}
