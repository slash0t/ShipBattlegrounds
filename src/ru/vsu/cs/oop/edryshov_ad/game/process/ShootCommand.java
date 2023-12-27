package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Game;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

public class ShootCommand extends ShipCommand {
    private final Cell target;

    public ShootCommand(Player player, Ship ship, Game game, Cell target) {
        super(player, ship, game);
        this.target = target;
    }

    @Override
    void execute(Game game) {
        game.shipShoot(getPlayer(), getShip(), target);
    }

    @Override
    void undo(Game game) {
        game.shipUndoShoot(getShip(), target);
    }

    @Override
    public String toString() {
        return String.format("Игрок %s выстрелил кораблем %s в %s", getPlayer(), getShip(), target);
    }
}
