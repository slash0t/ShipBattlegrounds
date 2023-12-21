package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.CommandCreator;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipCommand;

import java.util.LinkedList;
import java.util.Queue;

public class TurningAroundController implements PlayerController {
    @Override
    public Queue<ShipCommand> doMove(Player player, CommandCreator commandCreator, Field field) {
        Queue<ShipCommand> commands = new LinkedList<>();

        for (Ship ship : player.getActiveShips()) {
            ShipCommand command = commandCreator.createTurnCommand(player, ship, true);
            command.execute();
            commands.add(command);
        }

        return commands;
    }
}
