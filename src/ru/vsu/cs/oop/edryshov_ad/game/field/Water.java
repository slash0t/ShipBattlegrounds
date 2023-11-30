package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public class Water extends Cell {
    private final int depth;

    private Ship ship;

    public Water(int x, int y, int depth) {
        super(x, y);

        this.depth = depth;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    boolean canShipSailThrough(Ship ship) {
        if (ship.getMinSailDepth() > depth) {
            return false;
        }

        return this.ship == null || this.ship == ship;
    }
}
