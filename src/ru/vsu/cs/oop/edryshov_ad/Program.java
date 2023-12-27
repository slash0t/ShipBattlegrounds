package ru.vsu.cs.oop.edryshov_ad;

import ru.vsu.cs.oop.edryshov_ad.game.*;
import ru.vsu.cs.oop.edryshov_ad.game.player.DefaultPlayerController;
import ru.vsu.cs.oop.edryshov_ad.game.player.SailingForwardController;
import ru.vsu.cs.oop.edryshov_ad.game.player.TurningAroundController;

import java.util.*;

public class Program {
    public static void main(String[] args) {
        GameParser parser = new GameParser.Builder().
                withOrderedPick().
                withControllers(List.of(
//                        new SailingForwardController(),
//                        new TurningAroundController(),
                        new DefaultPlayerController()
                ))
                .build();

        Game game = parser.parseGame("games/game2.txt");

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

            if (game.getGameState() != GameState.ONGOING) {
                System.out.printf("Выиграл игрок %s%n", game.getWinners().get(0));
            }
        } while (!query.equals("exit"));

        System.out.println();
    }
}
