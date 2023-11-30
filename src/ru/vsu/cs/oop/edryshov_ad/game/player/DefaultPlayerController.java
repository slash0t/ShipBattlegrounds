package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.GameStep;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;

import java.util.Collection;

public class DefaultPlayerController implements PlayerController {
    @Override
    public GameStep doMove(Field field, Collection<Ship> ships) {
        return null;
    }
}
