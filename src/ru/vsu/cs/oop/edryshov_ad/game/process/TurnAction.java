package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public class TurnAction extends ShipAction {
    private final boolean right;

    public TurnAction(Ship ship, boolean right) {
        super(ship);
        this.right = right;
    }

    @Override
    public String toString() {
        String side = right ? "right" : "left";
        return String.format("%s: turns %s", getShip().toString(), side);
    }
}
