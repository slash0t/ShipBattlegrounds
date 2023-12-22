package ru.vsu.cs.oop.edryshov_ad;

import ru.vsu.cs.oop.edryshov_ad.game.*;
import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.field.Rock;
import ru.vsu.cs.oop.edryshov_ad.game.field.Water;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;
import ru.vsu.cs.oop.edryshov_ad.game.player.SailingForwardController;
import ru.vsu.cs.oop.edryshov_ad.game.player.TurningAroundController;

import java.util.*;

public class Program {
    public static void main(String[] args) {
        Game game = getExampleGame(3);

        System.out.println(game);

        Scanner scanner = new Scanner(System.in);

        String query;
        do {
            query = scanner.next();

            if (query.equals("n")) {
                game.playStepForward();
            } else if (query.equals("b")) {
                game.playStepBack();
            }

            System.out.println(game);
        } while (!query.equals("exit") && game.getGameState() == GameState.ONGOING);

        System.out.println(game.getWinners());
    }

    private static Game getExampleGame(int playerCount) {
        Field field = getExampleField(3 + playerCount * 2, 3 + playerCount * 2);
        Queue<Player> players = getExamplePlayers(playerCount);

        Cell curr = field.getStartCell().getBottom().getBottom().getRight().getRight();
        LinkedList<ArrayList<Water>> shipsToPlace = new LinkedList<>();
        for (int i = 0; i < playerCount; i++) {
            ArrayList<Water> list = new ArrayList<>(2);
            list.add((Water) curr.getBottom());
            list.add((Water) curr);
            shipsToPlace.add(list);
            curr = curr.getRight().getRight();
        }

        Iterator<Player> playerIterator = players.iterator();
        Iterator<ArrayList<Water>> placementIterator = shipsToPlace.iterator();
        for (int i = 0; i < playerCount; i++) {
            Player player = playerIterator.next();
            ArrayList<Water> placement = placementIterator.next();

            Ship ship = new Ship.Builder(i, 2, CardinalDirection.SOUTH).
                    withFiringRange(3).
                    withSailRange(2).
                    withMinSailDepth(100).
                    withStartHealth(1).
                    build();

            player.addActiveShip(ship);
            field.moveShipTo(ship, placement.get(0), CardinalDirection.NORTH);
        }

        return new Game(field, players);
    }

    private static Field getExampleField(int width, int height) {
        Cell start = new Rock(0, 0);
        Cell left = start;
        for (int i = 1; i < width; i++) {
            Cell curr = new Rock(i, 0);
            curr.setLeft(left);
            left.setRight(curr);
            left = curr;
        }
        Cell top = start;
        for (int i = 1; i < width; i++) {
            Cell curr = new Rock(0, i);
            curr.setTop(top);
            top.setBottom(curr);
            top = curr;
        }

        Cell anchorLeft = start.getBottom();
        Cell anchorTop = start.getRight();

        for (int i = 1; i < height; i++) {
            left = anchorLeft;
            top = anchorTop;
            for (int j = 1; j < width; j++) {
                Cell curr;
                if (i == width - 1 || j == height - 1) {
                    curr = new Rock(i, j);
                } else {
                    curr = new Water(i, j, 1000);
                }

                curr.setLeft(left);
                left.setRight(curr);
                left = left.getRight();

                curr.setTop(top);
                top.setBottom(curr);
                top = top.getRight();
            }
            anchorLeft = anchorLeft.getBottom();
            anchorTop = anchorTop.getBottom();
        }

        return new Field(start, width, height);
    }

    private static Queue<Player> getExamplePlayers(int count) {
        Queue<Player> players = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                players.add(new Player(i,  new TurningAroundController()));
            } else {
                players.add(new Player(i, new SailingForwardController()));
            }
        }
        return players;
    }
}
