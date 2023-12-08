package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.process.GameStep;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;

import java.util.Collection;

public interface PlayerController {
    GameStep doMove(Player player, Field field, Collection<Ship> ships);
}
