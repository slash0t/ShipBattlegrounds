package ru.vsu.cs.oop.edryshov_ad.game;

import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.player.Player;
import ru.vsu.cs.oop.edryshov_ad.game.process.GameStep;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private final Field field;
    private final Queue<Player> players;

    private final BufferedReader fileInput;
    private final Queue<GameStep> gameSteps;

    private boolean isGameEnded;
    private Player winner;

    public Game(Field field, Queue<Player> players) {
        this.field = field;
        this.players = players;

        this.fileInput = null;
        this.gameSteps = new LinkedList<>();

        this.isGameEnded = false;
        this.winner = null;
    }

    public Game(Field field, Queue<Player> players, BufferedReader fileInput) {
        this.field = field;
        this.players = players;
        this.fileInput = fileInput;

        this.gameSteps = new LinkedList<>();

        this.isGameEnded = false;
        this.winner = null;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public Player getWinner() {
        return winner;
    }

    public void playStep() {
        if (isGameEnded) {
            return;
        }

        Player player = players.poll();

        if (fileInput == null) {
            GameStep step = player.doMove(field);
            gameSteps.add(step);
        } else {
            //TODO чтение из файла игры
        }

        players.add(player);

        Player winner = findWinner();
        if (winner != null) {
            isGameEnded = true;
            this.winner = winner;
        }
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

    @Override
    public String toString() {
        return field.toString();
    }

    //TODO сохранение игры
}
