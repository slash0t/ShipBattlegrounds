package ru.vsu.cs.oop.edryshov_ad.game;

import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.field.FieldException;
import ru.vsu.cs.oop.edryshov_ad.game.field.Water;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;
import ru.vsu.cs.oop.edryshov_ad.game.process.CommandCreator;
import ru.vsu.cs.oop.edryshov_ad.game.process.ShipCommand;

import java.util.*;

public class Game {
    private final double EPSILON = 1e-6f;

    private final Field field;
    private final Queue<Player> players;

    private final Stack<ShipCommand> commandsHistory;
    private final Stack<ShipCommand> upcomingCommands;

    private GameState gameState;
    private List<Player> winners;

    public Game(Field field, Queue<Player> players) {
        this.field = field;
        this.players = players;

        CommandCreator creator = new CommandCreator(this);
        for (Player player : players) {
            player.setCommandCreator(creator);
        }

        this.commandsHistory = new Stack<>();
        this.upcomingCommands = new Stack<>();

        this.gameState = GameState.ONGOING;
        this.winners = new LinkedList<>();
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Player> getWinners() {
        return winners;
    }

    private int playPlayerStep() {
        if (gameState != GameState.ONGOING || players.size() == 0) {
            return 0;
        }

        Player player = players.poll();

        Queue<ShipCommand> commandsQueue = player.doMove(field);

        commandsHistory.addAll(commandsQueue);

        players.add(player);

        Player winner = findWinner();
        if (winner != null) {
            gameState = GameState.ENDED_WITH_VICTORY;
            winners.add(winner);
        }

        return commandsHistory.size();
    }

    public void playStepBack() {
        if (commandsHistory.isEmpty()) {
            return;
        }

        ShipCommand command = commandsHistory.pop();
        command.undo();
        upcomingCommands.add(command);
    }

    public void playStepForward() {
        if (upcomingCommands.isEmpty()) {
            int stepsPlayed = playPlayerStep();

            for (int i = 0; i < stepsPlayed - 1; i++) {
                playStepBack();
            }

            return;
        }

        ShipCommand command = upcomingCommands.pop();
        command.execute();
        commandsHistory.add(command);
    }

    private Player findWinner() {
        Player winner = null;

        for (Player player : players) {
            if (player.lost()) {
                continue;
            }

            if (winner == null) {
                winner = player;
            } else {
                return null;
            }
        }

        //TODO ситуация ничьей
        return winner;
    }

    public void sailShip(Player player, Ship ship, int range) throws FieldException {
        if (!player.containsActiveShip(ship)) {
            return;
        }

        int stepsPossible = 0;
        Cell now = field.getShipHead(ship);
        Vector2 direction = ship.getDirection();

        boolean stopped = false;
        while (stepsPossible < Math.min(range, ship.getSailRange()) && !stopped) {
            now = now.getFromDirection(direction);

            SailingResult result = now.canShipSailThrough(ship);

            switch (result) {
                case BUMPED -> {
                    ship.changeHealth(-1);

                    if (ship.isSunk()) {
                        player.removeActiveShip(ship);
                        player.addDeadShip(ship);
                    }

                    stopped = true;
                    now = now.getFromDirection(direction.getNegative());
                }
                case STUCK -> {
                    player.removeActiveShip(ship);
                    player.addStuckShip(ship);

                    stopped = true;
                }
            }

            stepsPossible++;
        }

        field.moveShipTo(ship, now, direction.getNegative());
    }

    public void sailShipBack(Player player, Ship ship, int range) {

    }

    public void turnShip(Player player, Ship ship, boolean right) {
        if (!player.containsActiveShip(ship)) {
            return;
        }

        int size = ship.getSize();
        Vector2 direction = ship.getDirection();

        if (size == 1) {
            ship.setDirection(direction.getRotated(right));
            return;
        }

        ArrayList<Water> cellPositions = field.getShipCells(ship);

        Cell midCell = cellPositions.get(size / 2);

        int upperRectSize = size / 2 + 1;
        int lowerRectSize = size - size / 2;

        Vector2 newDirection = direction.getRotated(right);

        SailingResult upperResult = field.getSailingResultInRect(
                ship, midCell, upperRectSize,
                newDirection, direction
        );
        SailingResult lowerResult = field.getSailingResultInRect(
                ship, midCell, lowerRectSize,
                newDirection.getNegative(), direction.getNegative()
        );

        if (upperResult == SailingResult.BUMPED || lowerResult == SailingResult.BUMPED) {
            ship.changeHealth(-1);
        }

        if (upperResult == SailingResult.SAILED && lowerResult == SailingResult.SAILED) {
            Cell newHead = midCell;
            for (int i = 1; i < upperRectSize; i++) {
                newHead = newHead.getFromDirection(newDirection);
            }

            field.moveShipTo(ship, newHead, newDirection.getNegative());
            ship.setDirection(newDirection);
        }
    }

    public void turnShipBack(Player player, Ship ship, boolean right) {

    }

    public void shipShoot(Player player, Ship ship, Cell target) {
        if (!player.containsActiveShip(ship) && !player.containsStuckShip(ship)) {
            return;
        }

        Vector2 shipHeadPos = field.getShipHead(ship).getPosition();

        if (shipHeadPos.distanceTo(target.getPosition()) - ship.getFiringRange() > EPSILON) {
            return;
        }

        if (target instanceof Water water) {
            Ship targetShip = water.getShip();

            if (targetShip != null) {
                targetShip.changeHealth(-1);
            }
        }
    }

    public void shipUndoShoot(Player player, Ship ship, Cell target) {

    }

    @Override
    public String toString() {
        return field.toString();
    }

    //TODO сохранение игры
}
