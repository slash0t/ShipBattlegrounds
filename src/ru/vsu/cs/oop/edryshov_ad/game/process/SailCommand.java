package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Game;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

public class SailCommand extends ShipCommand {
    private final int range;
    private int sailedRange;

    public SailCommand(Player player, Ship ship, Game game, int range) {
        super(player, ship, game);
        this.range = range;
    }

    @Override
    void execute(Game game) {
        sailedRange = game.sailShip(getPlayer(), getShip(), range);
    }

    @Override
    void undo(Game game) {
        game.sailShipBack(getPlayer(), getShip(), range, sailedRange);
    }
}
