package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Puzzle2 {
    public static final URL INPUT_URL = Utils.newURL("https://adventofcode.com/2023/day/2/input");
    public static final int MAX_REDS = 12, MAX_GREENS = 13, MAX_BLUES = 14;

    public static void main(final String... args) {
        Utils.PuzzleIntPair answer = solve();
        System.out.println("Answer for first half of puzzle 2: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 2: " + answer.secondHalf());
    }

    public static Utils.PuzzleIntPair solve() {
        int result = 0;
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                Game game = Game.parse(inputLine);
                for (CubeSet cubeSet : game.rolls())
                    if (cubeSet.red() >= MAX_REDS ||
                        cubeSet.green() >= MAX_GREENS ||
                        cubeSet.blue() >= MAX_BLUES
                    ) {
                        System.out.println(game);
                        result += game.gameNum();
                        break;
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Utils.PuzzleIntPair(result, 0); // zero bcuz i havent finished first half
    }

    public record CubeSet(int red, int green, int blue) {
        private static final Pattern CUBES_PATTERN = Pattern.compile("(\\d+)(red|green|blue)");
        public static CubeSet parse(String cubeSet) {
            int red = 0, green = 0, blue = 0;
            String[] colors = cubeSet.toLowerCase().replace(" ", "").split(",");
            if (colors.length > 3 || colors.length < 1)
                throw new IllegalArgumentException();
            for (String str : colors) {
                if (str.isEmpty())
                    continue;
                Matcher matcher = CUBES_PATTERN.matcher(str);
                if (!matcher.matches())
                    throw new IllegalArgumentException(cubeSet);
                switch (matcher.group(2)) {
                    case "red" -> red = Integer.parseInt(matcher.group(1));
                    case "green" -> green = Integer.parseInt(matcher.group(1));
                    case "blue" -> blue = Integer.parseInt(matcher.group(1));
                }
            }
            return new CubeSet(red, green, blue);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (red != 0) {
                builder.append(red);
                builder.append(" red");
            }
            if (blue != 0) {
                builder.append(", ");
                builder.append(blue);
                builder.append(" blue");
            }
            if (green != 0) {
                builder.append(", ");
                builder.append(green);
                builder.append(" green");
            }
            return builder.toString();
        }
    }

    public record Game(int gameNum, CubeSet... rolls) {
        public static Game parse(String game) {
            int index = game.indexOf(':');
            final int gameNum = Integer.parseInt(game.substring(5, index));
            game = game.substring(index + 1);
            return new Game(gameNum,
                Arrays.stream(game.split(";"))
                      .map(CubeSet::parse)
                      .toArray(CubeSet[]::new)
            );
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("Game ");
            builder.append(gameNum);
            builder.append(": ");
            for (int i = 0; i < rolls.length; i++)  {
                builder.append(rolls[i]);
                if (i < rolls.length - 1)
                    builder.append("; ");
            }
            return builder.toString();
        }
    }

    private Puzzle2() {
        throw new AssertionError("Cannot instantiate Puzzle2!");
    }
}
