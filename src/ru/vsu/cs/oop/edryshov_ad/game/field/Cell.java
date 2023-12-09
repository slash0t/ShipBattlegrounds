package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.Vector2;

public abstract class Cell implements Comparable<Cell> {
    private Cell right;
    private Cell left;
    private Cell top;
    private Cell bottom;

    private final Vector2 position;

    Cell(int x, int y) {
        this.position = new Vector2(x, y);
    }

    public abstract SailingResult canShipSailThrough(Ship ship);

    public Cell getRight() {
        return right;
    }

    public Cell getLeft() {
        return left;
    }

    public Cell getTop() {
        return top;
    }

    public Cell getBottom() {
        return bottom;
    }

    public Cell getFromDirection(Vector2 direction) {
        int x = direction.getX(), y = direction.getY();

        if (x * x + y * y != 1) {
            return null;
        }

        if (x == 1) {
            return getRight();
        } else if (x == -1) {
            return getLeft();
        } else if (y == 1) {
            return getTop();
        } else {
            return getBottom();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setRight(Cell right) {
        this.right = right;
    }

    public void setLeft(Cell left) {
        this.left = left;
    }

    public void setTop(Cell top) {
        this.top = top;
    }

    public void setBottom(Cell bottom) {
        this.bottom = bottom;
    }

    @Override
    public int compareTo(Cell cell) {
        Vector2 cellPosition = cell.getPosition();
        if (position.getX() == cellPosition.getX()) {
            return Integer.compare(position.getY(), cellPosition.getY());
        }
        return Integer.compare(position.getX(), cellPosition.getX());
    }

    @Override
    public String toString() {
        return String.format("%d %d", getPosition().getX(),  getPosition().getY());
    }
}
