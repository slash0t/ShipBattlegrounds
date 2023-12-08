package ru.vsu.cs.oop.edryshov_ad.game.process;

import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

import java.util.Queue;
import java.util.StringJoiner;

public class GameStep {
    public static final String STEP_START = "Start step";
    public static final String STEP_END = "End step";

    private final Player player;
    private final Queue<ShipAction> actions;

    public GameStep(Player player, Queue<ShipAction> actions) {
        this.player = player;
        this.actions = actions;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        StringJoiner actionLines = new StringJoiner("%n");

        for (ShipAction action : actions) {
            actionLines.add(action.toString());
        }

        return String.format(
                "%s%n%s performed:%n%s%n%s",
                STEP_START, player.toString(), actionLines, STEP_END
        );
    }
}
