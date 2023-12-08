package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public class SailAction extends ShipAction {
    private final int range;

    public SailAction(Ship ship, int range) {
        super(ship);
        this.range = range;
    }

    @Override
    public String toString() {
        return String.format("%s: sail %d", getShip().toString(), range);
    }
}
