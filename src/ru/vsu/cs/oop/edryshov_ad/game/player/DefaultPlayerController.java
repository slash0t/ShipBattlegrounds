package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.process.GameStep;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.SailAction;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipAction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class DefaultPlayerController implements PlayerController {
    @Override
    public GameStep doMove(Player player, Field field, Collection<Ship> ships) {
        Queue<ShipAction> actions = new LinkedList<>();

        for (Ship ship : ships) {
            actions.add(new SailAction(ship, 1));
            ship.sail(1);
        }

        return new GameStep(player, actions);
    }
}
