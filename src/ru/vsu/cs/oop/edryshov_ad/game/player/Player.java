package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.process.GameStep;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;

import java.util.TreeSet;

public class Player {
    private final int id;

    private final PlayerController controller;

    private TreeSet<Ship> ships;

    public Player(int id, PlayerController controller) {
        this.id = id;
        this.controller = controller;
        this.ships = new TreeSet<>();
    }

    public void assignShip(Ship ship) {
        if (ship.getPlayer() == this) {
            ships.add(ship);
        }
    }

    public GameStep doMove(Field field) {
        return controller.doMove(this, field, ships);
    }

    public boolean lost() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Player %d", id);
    }
}
