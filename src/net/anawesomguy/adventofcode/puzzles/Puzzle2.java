package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Puzzle2 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/2/input");
    public static final int MAX_REDS = 12, MAX_GREENS = 13, MAX_BLUES = 14;

    public static void main(final String... args) {
        Utils.PuzzleIntPair answer = solve();
        System.out.println("Answer for first half of puzzle 2: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 2: " + answer.secondHalf());
    }

    public static Utils.PuzzleIntPair solve() {
        int result1 = 0, result2 = 0;
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            Game game;
            while ((game = Game.parse(br.readLine())) != null) {
                boolean possible = true;
                int highestRed = 1, highestGreen = 1, highestBlue = 1;
                for (CubeSet cubeSet : game.rolls()) {
                    // first half
                    if (cubeSet.red() > MAX_REDS ||
                        cubeSet.green() > MAX_GREENS ||
                        cubeSet.blue() > MAX_BLUES
                    ) possible = false;

                    // second half
                    if (cubeSet.red() > highestRed)
                        highestRed = cubeSet.red();
                    if (cubeSet.green() > highestGreen)
                        highestGreen = cubeSet.green();
                    if (cubeSet.blue() > highestBlue)
                        highestBlue = cubeSet.blue();
                }
                if (possible)
                    result1 += game.gameNum();

                result2 += highestRed * highestGreen * highestBlue;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Utils.PuzzleIntPair(result1, result2);
    }

    public record CubeSet(int red, int green, int blue) {
        private static final Pattern CUBES_PATTERN = Pattern.compile("(\\d+)(red|green|blue)");
        public static CubeSet parse(String cubeSet) {
            if (cubeSet == null)
                return null;
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
            final boolean hasRed = red != 0,
                          hasBlue = blue != 0;
            if (hasRed) {
                builder.append(red);
                builder.append(" red");
            }
            if (hasBlue) {
                if (hasRed)
                    builder.append(", ");
                builder.append(blue);
                builder.append(" blue");
            }
            if (green != 0) {
                if (hasBlue || hasRed)
                    builder.append(", ");
                builder.append(green);
                builder.append(" green");
            }
            return builder.toString();
        }
    }

    public record Game(int gameNum, CubeSet... rolls) {
        public static Game parse(String game) {
            if (game == null)
                return null;
            String[] gameAndRounds = game.split(":", 2);
            final int gameNum = Integer.parseInt(gameAndRounds[0].substring(5));
            return new Game(gameNum,
                Arrays.stream(gameAndRounds[1].split(";"))
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
