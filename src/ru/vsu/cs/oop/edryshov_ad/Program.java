package ru.vsu.cs.oop.edryshov_ad;

import ru.vsu.cs.oop.edryshov_ad.game.*;

import java.util.*;

public class Program {
    public static void main(String[] args) {
        Game game = GameParser.parseGame("games/game1.txt");

        if (game == null) {
            System.out.println("Не удалось создать игру");
            return;
        }

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
}
