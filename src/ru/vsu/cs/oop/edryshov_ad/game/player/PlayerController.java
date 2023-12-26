package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.CommandCreator;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipCommand;

import java.util.Collection;
import java.util.Queue;

public interface PlayerController {
    Queue<ShipCommand> doMove(
            Collection<Ship> activeShips, Collection<Ship> stuckShips,
            CommandCreator commandCreator, Field field
    );
}
