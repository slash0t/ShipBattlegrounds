package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;

public abstract class ShipAction {
    private Ship ship;

    public ShipAction(Ship ship) {
        this.ship = ship;
    }

    Ship getShip() {
        return ship;
    }
}
