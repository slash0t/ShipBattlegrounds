package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.process.GameStep;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipAction;
import ru.vsu.cs.oop.edryshov_ad.game.process.TurnAction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class TurningAroundController  implements PlayerController {
    @Override
    public GameStep doMove(Player player, Field field, Collection<Ship> ships) {
        Queue<ShipAction> actions = new LinkedList<>();

        for (Ship ship : ships) {
            actions.add(new TurnAction(ship, true));
            ship.turn(true);
        }

        return new GameStep(player, actions);
    }
}
