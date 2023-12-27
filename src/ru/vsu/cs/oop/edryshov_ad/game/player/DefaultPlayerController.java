package ru.vsu.cs.oop.edryshov_ad.game.player;

import ru.vsu.cs.oop.edryshov_ad.game.CardinalDirection;
import ru.vsu.cs.oop.edryshov_ad.game.SailingResult;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.Vector2;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.field.Water;
import ru.vsu.cs.oop.edryshov_ad.game.process.*;

import java.util.*;
import java.util.function.Function;

public class DefaultPlayerController implements PlayerController {
    @Override
    public Queue<ShipCommand> doMove(
            Player player,
            Collection<Ship> activeShips, Collection<Ship> stuckShips,
            CommandCreator commandCreator, Field field
    ) {
        Queue<ShipCommand> queue = new LinkedList<>();

        Queue<ShipCommand> queueStuckShot = shootAsPossible(stuckShips, player, field, commandCreator);
        queue.addAll(queueStuckShot);

        Queue<ShipCommand> queueActiveShot = shootAsPossible(activeShips, player, field, commandCreator);
        queue.addAll(queueActiveShot);

        Set<Ship> activeShipLeft = new TreeSet<>(activeShips);
        queueActiveShot.forEach(command -> activeShipLeft.remove(command.getShip()));

        for (Ship ship : activeShipLeft) {
            ShipCommand command = moveTowardsCenter(ship, field, commandCreator);
            if (command != null) {
                queue.add(command);
            }
        }

        return queue;
    }

    private Queue<ShipCommand> shootAsPossible(
            Collection<Ship> ships, Player player,
            Field field, CommandCreator commandCreator
    ) {
        Queue<ShipCommand> queue = new LinkedList<>();

        Set<Map.Entry<Water, Ship>> mapShipSet = field.waterShipEntry();

        for (Ship ship : ships) {
            Vector2 position = field.getShipHead(ship).getPosition();

            for (Map.Entry<Water, Ship> entry : mapShipSet) {
                Ship enemy = entry.getValue();
                if (player.containsShip(enemy)) {
                    continue;
                }
                if (enemy.isSunk()) {
                    continue;
                }

                Water cell = entry.getKey();
                Vector2 enemyPosition = cell.getPosition();
                if (position.distanceTo(enemyPosition) - ship.getFiringRange() > Vector2.EPSILON) {
                    continue;
                }

                ShootCommand command = commandCreator.createShootCommand(ship, cell);
                command.execute();
                queue.add(command);
                break;
            }
        }

        return queue;
    }

    private ShipCommand moveTowardsCenter(Ship ship, Field field, CommandCreator commandCreator) {
        double centerX = (field.getWidth() - 1) / 2.0;
        double centerY = (field.getHeight() - 1) / 2.0;

        double shipX, shipY;
        int shipSize = ship.getSize();
        ArrayList<Water> shipCells = field.getShipCells(ship);
        if (shipSize % 2 == 1) {
            Vector2 position = shipCells.get(shipSize / 2).getPosition();
            shipX = position.getX();
            shipY = position.getY();
        } else {
            Vector2 position1 = shipCells.get(shipSize / 2).getPosition();
            Vector2 position2 = shipCells.get(shipSize / 2 - 1).getPosition();

            shipX = (position1.getX() + position2.getX()) / 2.0;
            shipY = (position1.getY() + position2.getY()) / 2.0;
        }

        CardinalDirection direction = ship.getDirection();

        if (isDirectedTowardsCenter(direction, shipX, shipY, centerX, centerY)) {
            int distanceToCenter;
            if (direction == CardinalDirection.NORTH || direction == CardinalDirection.SOUTH) {
                distanceToCenter = (int) Math.abs(shipY - centerY);
            } else {
                distanceToCenter = (int) Math.abs(shipX - centerX);
            }

            ShipCommand forwardCommand = tryGoForward(ship, field, commandCreator, distanceToCenter);
            if (forwardCommand != null) {
                forwardCommand.execute();
                return forwardCommand;
            }
        }

        ShipCommand turnCommand = tryTurn(
                ship, field, commandCreator,
                dir -> isDirectedTowardsCenter(dir, shipX, shipY, centerX, centerY)
        );

        if (turnCommand != null) {
            turnCommand.execute();
            return turnCommand;
        }

        return null;
    }

    private boolean isDirectedTowardsCenter(
            CardinalDirection direction,
            double shipX, double shipY,
            double centerX, double centerY
    ) {
        if (direction == CardinalDirection.NORTH && centerY - shipY > Vector2.EPSILON) {
            return true;
        }
        if (direction == CardinalDirection.SOUTH && shipY - centerY > Vector2.EPSILON) {
            return true;
        }
        if (direction == CardinalDirection.WEST && shipX - centerX > Vector2.EPSILON) {
            return true;
        }
        if (direction == CardinalDirection.EAST && centerX - shipX > Vector2.EPSILON) {
            return true;
        }
        return false;
    }

    private SailCommand tryGoForward(Ship ship, Field field, CommandCreator commandCreator, int distanceToCenter) {
        int sailRange = Math.min(ship.getSailRange(), distanceToCenter);

        while (field.getShipSailResult(ship, sailRange) != SailingResult.SAILED && sailRange > 0) {
            sailRange--;
        }

        if (sailRange == 0) {
            return null;
        }
        return commandCreator.createSailCommand(ship, sailRange);
    }

    private TurnCommand tryTurn(
            Ship ship, Field field, CommandCreator commandCreator,
            Function<CardinalDirection, Boolean> turnedTowardsCenter
    ) {
        CardinalDirection direction = ship.getDirection();

        CardinalDirection turnedRight = direction.getRight();
        if (
                field.getShipTurnSailingResult(ship, true) == SailingResult.SAILED &&
                turnedTowardsCenter.apply(turnedRight)
        ) {
            return commandCreator.createTurnCommand(ship, true);
        }

        CardinalDirection turnLeft = direction.getRight();
        if (
                field.getShipTurnSailingResult(ship, false) == SailingResult.SAILED &&
                turnedTowardsCenter.apply(turnLeft)
        ) {
            return commandCreator.createTurnCommand(ship, false);
        }

        return null;
    }
}
