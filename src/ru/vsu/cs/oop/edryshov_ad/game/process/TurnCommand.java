package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Game;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

public class TurnCommand extends ShipCommand {
    private final boolean right;
    private boolean turned;

    public TurnCommand(Player player, Ship ship, Game game, boolean right) {
        super(player, ship, game);
        this.right = right;
    }

    @Override
    void execute(Game game) {
        turned = game.turnShip(getPlayer(), getShip(), right);
    }

    @Override
    void undo(Game game) {
        game.turnShipBack(getPlayer(), getShip(), right, turned);
    }
}
