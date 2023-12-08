package ru.vsu.cs.oop.edryshov_ad;

import ru.vsu.cs.oop.edryshov_ad.game.Game;
import ru.vsu.cs.oop.edryshov_ad.game.Ship;
import ru.vsu.cs.oop.edryshov_ad.game.Vector2;
import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.field.Rock;
import ru.vsu.cs.oop.edryshov_ad.game.field.Water;
import ru.vsu.cs.oop.edryshov_ad.game.player.DefaultPlayerController;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Program {
    public static void main(String[] args) {
        Game game = getExampleGame(3);

        while (!game.isGameEnded()) {
            game.playStep();
            System.out.println(game);
        }

        System.out.println(game.getWinner());
    }

    private static Game getExampleGame(int playerCount) {
        Field field = getExampleField(1 + playerCount * 2, 1 + playerCount * 2);
        Queue<Player> players = getExamplePlayers(playerCount);

        Cell curr = field.getStartCell().getBottom().getRight();
        LinkedList<ArrayList<Water>> shipsToPlace = new LinkedList<>();
        for (int i = 0; i < playerCount; i++) {
            ArrayList<Water> list = new ArrayList<>(1);
            list.add((Water) curr);
            shipsToPlace.add(list);
            curr = curr.getRight().getRight();
        }

        Iterator<Player> playerIterator = players.iterator();
        Iterator<ArrayList<Water>> placementIterator = shipsToPlace.iterator();
        for (int i = 0; i < playerCount; i++) {
            Player player = playerIterator.next();
            ArrayList<Water> placement = placementIterator.next();

            Ship ship = new Ship(
                    i, 3, 2, 100, 1, 1,
                    field, player, new Vector2(0, -1), placement
            );

            player.assignShip(ship);
            field.moveShipTo(ship, placement.get(0), new Vector2(0, 1));
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
            players.add(new Player(i, new DefaultPlayerController()));
        }
        return players;
    }
}
