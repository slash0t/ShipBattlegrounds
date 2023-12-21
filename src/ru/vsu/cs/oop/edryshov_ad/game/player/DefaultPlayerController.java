package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.CommandCreator;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipCommand;

import java.util.Queue;

public class DefaultPlayerController implements PlayerController {
    @Override
    public Queue<ShipCommand> doMove(Player player, CommandCreator commandCreator, Field field) {
        return null;
    }
}
