package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public abstract class Barrier extends Cell {
    public Barrier(int x, int y) {
        super(x, y);
    }

    @Override
    boolean canShipSailThrough(Ship ship) {
        return false;
    }
}
