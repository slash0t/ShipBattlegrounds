package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Game;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

public abstract class ShipCommand {
    private final Player player;
    private final Ship ship;

    private final Game game;

    public ShipCommand(Player player, Ship ship, Game game) {
        this.player = player;
        this.ship = ship;
        this.game = game;
    }

    public Ship getShip() {
        return ship;
    }

    public Player getPlayer() {
        return player;
    }

    public void execute() {
        execute(game);
    }

    abstract void execute(Game game);

    public void undo() {
        undo(game);
    }

    abstract void undo(Game game);
}
