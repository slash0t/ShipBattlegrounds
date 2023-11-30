package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public abstract class Cell implements Comparable<Cell> {
    Cell right;
    Cell left;
    Cell top;
    Cell bottom;

    final int x;
    final int y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract boolean canShipSailThrough(Ship ship);

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        if (x == cell.getX()) {
            return Integer.compare(y, cell.getY());
        }
        return Integer.compare(x, cell.getX());
    }
}
