package ru.vsu.cs.oop.edryshov_ad.game;

import ru.vsu.cs.oop.edryshov_ad.game.field.Cell;
import ru.vsu.cs.oop.edryshov_ad.game.field.Field;
import ru.vsu.cs.oop.edryshov_ad.game.field.Rock;
import ru.vsu.cs.oop.edryshov_ad.game.field.Water;
import ru.vsu.cs.oop.edryshov_ad.game.player.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GameParser {
    private GameParser() {}

    private static final List<PlayerController> controllers = List.of(
            new SailingForwardController(),
            new TurningAroundController()
    );

    public static Game parseGame(String path) {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(path));
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Файл не найден");
            return null;
        }

        int height = scanner.nextInt();
        int width = scanner.nextInt();

        scanner.nextLine();
        Field field = parseField(scanner, width, height);

        scanner.next();
        int playerCount = scanner.nextInt();
        Player[] players = new Player[playerCount];

        Random random = new Random();
        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player(
                    i, controllers.get(random.nextInt(controllers.size()))
            );
        }

        scanner.nextLine();
        scanner.next();
        int shipsCount = scanner.nextInt();

        scanner.nextLine();
        parseShips(scanner, field, players, shipsCount);

        return new Game(field, new LinkedList<>(List.of(players)));
    }

    private static Field parseField(Scanner scanner, int width, int height) {
        if (width < 1 || height < 1) {
            return null;
        }

        Cell upperAnchor = null;
        for (int i = 0; i < height; i++) {
            String line = scanner.nextLine();

            Cell left = null;
            Cell upper = upperAnchor;
            for (int j = 0; j < width; j++) {
                Cell curr;

                switch (line.charAt(j)) {
                    case '#' -> curr = new Rock(j, height - i - 1);
                    case 'W' -> curr = new Water(j, height - i - 1, 150);
                    default -> curr = new Rock(j, height - i - 1);
                }

                if (j == 0) {
                    upperAnchor = curr;
                }

                if (left != null) {
                    curr.setLeft(left);
                    left.setRight(curr);
                }
                left = curr;

                if (upper != null) {
                    curr.setTop(upper);
                    upper.setBottom(curr);

                    upper = upper.getRight();
                }
            }
        }

        return new Field(upperAnchor, width, height);
    }

    private static void parseShips(Scanner scanner, Field field, Player[] players, int count) {
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();

            Pattern pattern = Pattern.compile("([A-Z_]+)=(\\d+);\\s*");
            Matcher matcher = pattern.matcher(line);

            TreeMap<String, Integer> map = new TreeMap<>();

            while (matcher.find()) {
                map.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
            }

            Pattern patternPosition = Pattern.compile("[A-Z_]+=\\((\\d+),\\s(\\d+)\\);\\s*");
            Matcher matcherPosition = patternPosition.matcher(line);

            if (!matcherPosition.find()) {
                continue;
            }
            Vector2 position = new Vector2(
                    Integer.parseInt(matcherPosition.group(1)),
                    Integer.parseInt(matcherPosition.group(2))
            );

            Pattern patternDirection = Pattern.compile("[A-Z_]+=([A-Z]+);\\s*");
            Matcher matcherDirection = patternDirection.matcher(line);

            if (!matcherDirection.find()) {
                continue;
            }
            CardinalDirection direction = CardinalDirection.getFromString(matcherDirection.group(1));

            Ship.Builder shipBuilder = new Ship.Builder(
                    map.get("ID"),
                    map.get("SIZE"),
                    direction
            );

            if (map.containsKey("FIRING_RANGE")) {
                shipBuilder.withFiringRange(map.get("FIRING_RANGE"));
            }
            if (map.containsKey("SAIL_RANGE")) {
                shipBuilder.withSailRange(map.get("SAIL_RANGE"));
            }
            if (map.containsKey("MIN_SAIL_DEPTH")) {
                shipBuilder.withMinSailDepth(map.get("MIN_SAIL_DEPTH"));
            }
            if (map.containsKey("HEALTH")) {
                shipBuilder.withStartHealth(map.get("HEALTH"));
            }

            Ship ship = shipBuilder.build();

            field.moveShipTo(
                    ship,
                    field.getCellFromCoordinates(position.getX(), position.getY()),
                    direction.getNegative()
            );

            players[map.get("TO_PLAYER") - 1].addActiveShip(ship);
        }
    }
}
