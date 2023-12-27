package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.CardinalDirection;
import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;

import java.util.*;

public class Field {
    private final Cell startCell;

    private final Map<Ship, ArrayList<Water>> shipMap;
    private final Map<Water, Ship> waterMap;

    private final int width;
    private final int height;

    public Field(Cell startCell, int width, int height) {
        this.startCell = startCell;
        this.shipMap = new TreeMap<>();
        this.waterMap = new TreeMap<>();
        this.width = width;
        this.height = height;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public ArrayList<Water> getShipCells(Ship ship) {
        return shipMap.get(ship);
    }

    public Water getShipHead(Ship ship) {
        return shipMap.get(ship).get(0);
    }

    public Ship getShipOnWater(Water water) {
        return waterMap.get(water);
    }

    public Set<Map.Entry<Water, Ship>> waterShipEntry() {
        return waterMap.entrySet();
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

        ArrayList<Water> positions = shipMap.get(ship);

        positions.forEach(Water::removeShip);
        positions.forEach(waterMap::remove);

        shipMap.remove(ship);
    }

    private void addShipCoordinate(Ship ship, Water water) {
        ArrayList<Water> cells = shipMap.getOrDefault(ship, new ArrayList<>());
        cells.add(water);
        water.setShip(ship);
        shipMap.put(ship, cells);
        waterMap.put(water, ship);
    }

    public Cell getCellFromCoordinates(int x, int y) {
        Cell cell = startCell;
        for (int i = 0; i < x; i++) {
            cell = cell.getRight();
        }
        for (int i = 0; i < y; i++) {
            cell = cell.getTop();
        }
        return cell;
    }

    public void moveShipTo(Ship ship, Cell startCell, CardinalDirection direction) throws FieldException {
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
        newPositions.forEach(water -> addShipCoordinate(ship, water));
    }

    public SailingResult getShipSailResult(Ship ship, int range) {
        Cell curr = getShipHead(ship);

        SailingResult result = SailingResult.SAILED;

        for (int i = 0; i < range && result == SailingResult.SAILED; i++) {
            curr = curr.getFromDirection(ship.getDirection());

            result = curr.canShipSailThrough(ship);
        }
        return result;
    }

    public SailingResult getShipTurnSailingResult(Ship ship, boolean right)  {
        SailingResult upperResult = getShipSailingResultInRect(ship, right, true);
        SailingResult lowerResult = getShipSailingResultInRect(ship, right, false);

        SailingResult result = SailingResult.SAILED;

        if (upperResult == SailingResult.STUCK || lowerResult == SailingResult.STUCK) {
            result = SailingResult.STUCK;
        }

        if (upperResult == SailingResult.BUMPED || lowerResult == SailingResult.BUMPED) {
            result = SailingResult.BUMPED;
        }

        return result;
    }

    private SailingResult getShipSailingResultInRect(Ship ship, boolean right, boolean upper) {
        int size = ship.getSize();
        CardinalDirection direction = ship.getDirection();

        Cell midCell = getShipCells(ship).get(size / 2);

        int rectSize;
        if (upper) {
            rectSize = size / 2 + 1;
        } else {
            rectSize = size - size / 2;
        }

        CardinalDirection newDirection = direction.getRotated(right);

        if (!upper) {
            direction = direction.getNegative();
            newDirection = newDirection.getNegative();
        }

        return getSailingResultInRect(
                ship, midCell, rectSize,
                newDirection, direction
        );
    }

    private SailingResult getSailingResultInRect(
            Ship ship, Cell startCell,
            int size, CardinalDirection horizontalDir, CardinalDirection verticalDir
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

        for (int i = 0; i < height - 1; i++) {
            anchor = anchor.getTop();
        }

        StringJoiner sj = new StringJoiner("\n");

        for (int i = 0; i < height; i++) {
            Cell curr = anchor;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < width; j++) {
                if (curr instanceof Water water) {
                    Ship ship = getShipOnWater(water);
                    if (ship == null) {
                        sb.append("\uD83C\uDF0A");
                    } else {
                        if (ship.isSunk()) {
                            sb.append("❌");
                        } else {
                            sb.append("\uD83D\uDEA2");
                        }
                    }
                } else {
                    sb.append("\uD83C\uDFD4");
                }
                curr = curr.getRight();
            }
            anchor = anchor.getBottom();
            sj.add(sb);
        }

        return sj.toString();
    }
}
