package ru.vsu.cs.oop.edryshov_ad.game;

import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.field.FieldException;
import ru.vsu.cs.oop.edryshov_ad.game.field.Water;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

import java.util.ArrayList;

public class Ship implements Comparable<Ship> {
    private final int id;
    private final int firingRange;
    private final int sailRange;
    private final int minSailDepth;
    private final int size;

    private final Field field;
    private final Player player;

    private int health;

    private boolean canMove;

    private Vector2 direction;

    private ArrayList<Water> cellPositions;

    //TODO сделать билдер с дефолтными значениями
    public Ship(
            int id, int firingRange, int sailRange, int minSailDepth, int size, int health,
            Field field, Player player, Vector2 direction, ArrayList<Water> cellPositions
    ) {
        this.id = id;
        this.firingRange = firingRange;
        this.sailRange = sailRange;
        this.minSailDepth = minSailDepth;
        this.size = size;
        this.health = health;

        this.field = field;
        this.player = player;
        this.direction = direction;
        this.cellPositions = cellPositions;

        this.canMove = true;
    }

    public int getId() {
        return id;
    }

    public int getFiringRange() {
        return firingRange;
    }

    public int getSailRange() {
        return sailRange;
    }

    public int getSize() {
        return size;
    }

    public Player getPlayer() {
        return player;
    }

    public int getHealth() {
        return health;
    }

    public boolean canMove() {
        return canMove;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public int getMinSailDepth() {
        return minSailDepth;
    }

    public boolean isSunk() {
        return health <= 0;
    }

    public void getDamage(int damage) {
        if (isSunk()) {
            return;
        }

        health -= damage;
        canMove = !isSunk();
    }

    public void sail(int range) throws FieldException {
        if (!canMove || isSunk()) {
            //TODO ошибка при некорректном действии
            return;
        }

        int stepsPossible = 0;
        Cell now = cellPositions.get(0);

        boolean stopped = false;
        while (stepsPossible < Math.min(range, sailRange) && !stopped) {
            now = now.getFromDirection(direction);

            SailingResult result = now.canShipSailThrough(this);

            switch (result) {
                case BUMPED -> {
                    getDamage(1);
                    stopped = true;
                    now = now.getFromDirection(direction.getNegative());
                }
                case STUCK -> {
                    canMove = false;
                    stopped = true;
                }
            }

            stepsPossible++;
        }

        cellPositions = field.moveShipTo(this, now, direction.getNegative());
    }

    public void shoot(Cell cell) {
        if (isSunk()) {
            return;
        }

        if (cell instanceof Water water) {
            Ship ship = water.getShip();

            if (ship != null) {
                ship.getDamage(1);
            }
        }
    }

    private SailingResult getSailingResultInRect(Cell startCell, int size, Vector2 horizontalDir, Vector2 verticalDir) {
        SailingResult result = SailingResult.SAILED;
        Cell anchorCell = startCell;
        for (int i = 0; i < size && result == SailingResult.SAILED; i++) {
            anchorCell = anchorCell.getFromDirection(horizontalDir);

            Cell curr = anchorCell;

            for (int j = 0; j < size && result == SailingResult.SAILED; j++) {
                result = curr.canShipSailThrough(this);
                curr = curr.getFromDirection(verticalDir);
            }
        }

        return result;
    }

    public void turn(boolean right) {
        if (!canMove || isSunk()) {
            return;
        }

        if (size == 1) {
            direction = direction.getRotated(right);
            return;
        }

        Cell midCell = cellPositions.get(size / 2);

        int upperRectSize = size / 2 + 1;
        int lowerRectSize = size - size / 2;

        Vector2 newDirection = direction.getRotated(right);
        Vector2 oppositeDirection = new Vector2(-newDirection.getX(), -newDirection.getX());

        SailingResult upperResult = getSailingResultInRect(
                midCell, upperRectSize,
                newDirection, direction
        );
        SailingResult lowerResult = getSailingResultInRect(
                midCell, lowerRectSize,
                oppositeDirection, direction.getNegative()
        );

        if (upperResult == SailingResult.BUMPED || lowerResult == SailingResult.BUMPED) {
            getDamage(1);
        }

        if (upperResult == SailingResult.SAILED && lowerResult == SailingResult.SAILED) {
            Cell newHead = midCell;
            for (int i = 1; i < upperRectSize; i++) {
                newHead = newHead.getFromDirection(newDirection);
            }

            cellPositions = field.moveShipTo(this, newHead, oppositeDirection);
        }
    }

    @Override
    public int compareTo(Ship ship) {
        return Integer.compare(id, ship.getId());
    }

    @Override
    public String toString() {
        return String.format("Ship %d", id);
    }
}
