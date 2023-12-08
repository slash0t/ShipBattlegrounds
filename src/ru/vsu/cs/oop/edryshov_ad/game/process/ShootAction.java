package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;

public class ShootAction extends ShipAction {
    private final Cell target;

    public ShootAction(Ship ship, Cell target) {
        super(ship);
        this.target = target;
    }

    @Override
    public String toString() {
        return String.format("%s: shoots %s", getShip().toString(), target.toString());
    }
}
