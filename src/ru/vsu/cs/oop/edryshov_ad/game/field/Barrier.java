package ru.vsu.cs.oop.edryshov_ad.game.field;

import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public abstract class Barrier extends Cell {
    Barrier(int x, int y) {
        super(x, y);
    }

    @Override
    public SailingResult canShipSailThrough(Ship ship) {
        return SailingResult.BUMPED;
    }
}
