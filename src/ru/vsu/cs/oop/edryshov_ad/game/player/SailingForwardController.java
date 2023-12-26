package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.CommandCreator;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipCommand;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class SailingForwardController implements PlayerController {
    @Override
    public Queue<ShipCommand> doMove(
            Collection<Ship> activeShips, Collection<Ship> stuckShips,
            CommandCreator commandCreator, Field field
    ) {
        Queue<ShipCommand> commands = new LinkedList<>();

        for (Ship ship : activeShips) {
            ShipCommand command = commandCreator.createSailCommand(ship, 1);
            command.execute();
            commands.add(command);
        }

        return commands;
    }
}
