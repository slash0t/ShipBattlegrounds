package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.CommandCreator;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipCommand;

import java.util.Queue;
import java.util.TreeSet;

public class Player {
    private final int id;

    private final PlayerController controller;

    private CommandCreator commandCreator;

    private final TreeSet<Ship> activeShips;
    private final TreeSet<Ship> stuckShips;
    private final TreeSet<Ship> deadShips;

    public Player(int id, PlayerController controller) {
        this.id = id;
        this.controller = controller;
//        this.commandCreator = commandCreator;

        this.activeShips = new TreeSet<>();
        this.stuckShips = new TreeSet<>();
        this.deadShips = new TreeSet<>();
    }

    public void setCommandCreator(CommandCreator commandCreator) {
        this.commandCreator = commandCreator;
    }

    public void addActiveShip(Ship ship) {
        activeShips.add(ship);
    }

    public void addStuckShip(Ship ship) {
        stuckShips.add(ship);
    }

    public void addDeadShip(Ship ship) {
        deadShips.add(ship);
    }

    public void removeActiveShip(Ship ship) {
        activeShips.remove(ship);
    }

    public void removeStuckShip(Ship ship) {
        stuckShips.remove(ship);
    }

    public void removeDeadShip(Ship ship) {
        deadShips.remove(ship);
    }

    public boolean containsActiveShip(Ship ship) {
        return activeShips.contains(ship);
    }

    public boolean containsStuckShip(Ship ship) {
        return stuckShips.contains(ship);
    }

    public boolean containsDeadShip(Ship ship) {
        return deadShips.contains(ship);
    }

    public boolean containsShip(Ship ship) {
        return activeShips.contains(ship) ||
                stuckShips.contains(ship) ||
                deadShips.contains(ship);
    }

    public TreeSet<Ship> getActiveShips() {
        return activeShips;
    }

    public TreeSet<Ship> getStuckShips() {
        return stuckShips;
    }

    public TreeSet<Ship> getDeadShips() {
        return deadShips;
    }

    public Queue<ShipCommand> doMove(Field field) {
        return controller.doMove(this, commandCreator, field);
    }

    public boolean lost() {
        return activeShips.size() == 0 && stuckShips.size() == 0;
    }

    @Override
    public String toString() {
        return String.format("Player %d", id);
    }
}
