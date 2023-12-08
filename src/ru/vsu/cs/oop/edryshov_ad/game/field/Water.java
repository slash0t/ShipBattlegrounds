package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public class Water extends Cell {
    private final int depth;

    private Ship ship;

    public Water(int x, int y, int depth) {
        super(x, y);

        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public Ship getShip() {
        return ship;
    }

    public void removeShip() {
        this.ship = null;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    public SailingResult canShipSailThrough(Ship ship) {
        if (ship.getMinSailDepth() > depth) {
            return SailingResult.STUCK;
        }

        if (this.ship == null || this.ship == ship) {
            return SailingResult.SAILED;
        } else {
            return SailingResult.BUMPED;
        }
    }
}
