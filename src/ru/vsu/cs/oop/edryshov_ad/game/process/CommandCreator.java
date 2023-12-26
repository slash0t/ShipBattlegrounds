package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.Game;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

public class CommandCreator {
    private final Game game;
    private final Player player;

    public CommandCreator(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public SailCommand createSailCommand(Ship ship, int range) {
        return new SailCommand(player, ship, game, range);
    }

    public TurnCommand createTurnCommand(Ship ship, boolean right) {
        return new TurnCommand(player, ship, game, right);
    }

    public ShootCommand createShootCommand(Ship ship, Cell target) {
        return new ShootCommand(player, ship, game, target);
    }
}
