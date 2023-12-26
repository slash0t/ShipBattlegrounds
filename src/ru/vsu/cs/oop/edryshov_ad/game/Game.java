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
    private final List<Player> winners;

    private final Stack<ShipCommand> commandsHistory;
    private final Stack<ShipCommand> upcomingCommands;

    private GameState gameState;

    public Game(Field field, Queue<Player> players) {
        this.field = field;
        this.players = players;

        players.forEach(player -> player.setCommandCreator(new CommandCreator(this, player)));

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

        if (!player.lost()) {
            players.add(player);
        }

        if (players.size() == 1) {
            gameState = GameState.ENDED_WITH_VICTORY;
            winners.add(players.peek());
        } else if (players.size() == 0) {
            gameState = GameState.ENDED_WITH_DRAW;
        }

        return commandsQueue.size();
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

    private void changeShipHealth(Ship ship, int difference) {
        Optional<Player> owner = players.stream().
                filter(p -> p.containsShip(ship)).
                findAny();

        if (owner.isEmpty()) {
            return;
        }
        changeShipHealth(owner.get(), ship, difference);
    }

    private void changeShipHealth(Player player, Ship ship, int difference) {
        ship.changeHealth(difference);

        if (difference > 0 && ship.getHealth() == difference) {
            setShipAlive(player, ship);
        } else if (difference < 0 && ship.isSunk()) {
            setShipDead(player, ship);
        }
    }

    private void setShipAlive(Player player, Ship ship) {
        player.removeDeadShip(ship);

        Optional<Water> water = field.getShipCells(ship).stream()
                .filter(w -> w.canShipSailThrough(ship) == SailingResult.STUCK)
                .findAny();
        SailingResult result = water.isPresent() ? SailingResult.STUCK : SailingResult.SAILED;

        if (result == SailingResult.SAILED) {
            player.addActiveShip(ship);
        } else {
            player.addStuckShip(ship);
        }
    }

    private void setShipDead(Player player, Ship ship) {
        player.removeActiveShip(ship);
        player.removeStuckShip(ship);

        player.removeDeadShip(ship);

        if (player.lost()) {
            players.remove(player);
        }
    }

    public int sailShip(Player player, Ship ship, int range) throws FieldException {
        if (!player.containsActiveShip(ship)) {
            return 0;
        }

        int stepsPossible = 0;
        Cell now = field.getShipHead(ship);
        CardinalDirection direction = ship.getDirection();

        boolean stopped = false;
        while (stepsPossible < Math.min(range, ship.getSailRange()) && !stopped) {
            now = now.getFromDirection(direction);

            SailingResult result = now.canShipSailThrough(ship);

            switch (result) {
                case BUMPED -> {
                    changeShipHealth(player, ship, -1);

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
                default -> stepsPossible++;
            }
        }

        field.moveShipTo(ship, now, direction.getNegative());

        return stepsPossible;
    }

    public void sailShipBack(Player player, Ship ship, int range, int sailedRange) {
        Water headCell = field.getShipHead(ship);
        if (
                sailedRange < Math.min(range, ship.getSailRange()) &&
                headCell.canShipSailThrough(ship) != SailingResult.STUCK
        ) {
            changeShipHealth(player, ship, 1);

            Cell next = headCell.getFromDirection(ship.getDirection());
            if (next instanceof Water water) {
                changeShipHealth(field.getShipOnWater(water), 1);
            }
        }

        CardinalDirection moveDir = ship.getDirection().getNegative();
        for (int i = 0; i < sailedRange; i++) {
            headCell = (Water) headCell.getFromDirection(moveDir);
        }
        field.moveShipTo(ship, headCell, moveDir);
    }

    private void turnShipPositions(Ship ship, boolean right) {
        int size = ship.getSize();
        int upperRectSize = size / 2 + 1;

        Cell newHead = field.getShipCells(ship).get(size / 2);

        CardinalDirection newDirection = ship.getDirection().getRotated(right);

        for (int i = 1; i < upperRectSize; i++) {
            newHead = newHead.getFromDirection(newDirection);
        }

        field.moveShipTo(ship, newHead, newDirection.getNegative());
        ship.setDirection(newDirection);
    }

    private SailingResult getShipSailingResultInRect(Ship ship, boolean right, boolean upper) {
        int size = ship.getSize();
        CardinalDirection direction = ship.getDirection();

        Cell midCell = field.getShipCells(ship).get(size / 2);

        int rectSize;
        if (upper) {
            rectSize = size / 2 + 1;
        } else {
            rectSize = size - size / 2;
        }

        CardinalDirection newDirection = direction.getRotated(right);

        if (!upper) {
            direction = direction.getNegative();
            newDirection = newDirection.getNegative();
        }

        return field.getSailingResultInRect(
                ship, midCell, rectSize,
                newDirection, direction
        );
    }

    public boolean turnShip(Player player, Ship ship, boolean right) {
        if (!player.containsActiveShip(ship)) {
            return false;
        }

        int size = ship.getSize();
        CardinalDirection direction = ship.getDirection();

        if (size == 1) {
            ship.setDirection(direction.getRotated(right));
            return true;
        }

        SailingResult upperResult = getShipSailingResultInRect(ship, right, true);
        SailingResult lowerResult = getShipSailingResultInRect(ship, right, false);

        if (upperResult == SailingResult.SAILED && lowerResult == SailingResult.SAILED) {
            turnShipPositions(ship, right);
            return true;
        }

        if (upperResult == SailingResult.BUMPED || lowerResult == SailingResult.BUMPED) {
            changeShipHealth(player, ship, -1);
        }

        return false;
    }

    public void turnShipBack(Player player, Ship ship, boolean right, boolean turned) {
        if (turned) {
            turnShipPositions(ship, !right);
            return;
        }

        SailingResult upperResult = getShipSailingResultInRect(ship, right, true);
        SailingResult lowerResult = getShipSailingResultInRect(ship, right, false);

        if (upperResult == SailingResult.BUMPED || lowerResult == SailingResult.BUMPED) {
            changeShipHealth(player, ship, 1);
        }
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
            Ship targetShip = field.getShipOnWater(water);

            if (targetShip != null) {
                changeShipHealth(targetShip, -1);
            }
        }
    }

    public void shipUndoShoot(Ship ship, Cell target) {
        Vector2 shipHeadPos = field.getShipHead(ship).getPosition();

        if (shipHeadPos.distanceTo(target.getPosition()) - ship.getFiringRange() > EPSILON) {
            return;
        }

        if (target instanceof Water water) {
            Ship targetShip = field.getShipOnWater(water);

            if (targetShip != null) {
                changeShipHealth(targetShip, 1);
            }
        }
    }

    @Override
    public String toString() {
        return field.toString();
    }

    //TODO сохранение игры
}
