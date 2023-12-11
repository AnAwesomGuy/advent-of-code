package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;

public final class Puzzle6 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/6/input");

    public static void main(final String... args) {
        Utils.PuzzlePair answer = solve();
        System.out.println("Answer for first half of puzzle 6: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 6: " + answer.secondHalf());
    }

    public static Utils.PuzzlePair solve() {
        long result1 = 1, result2;
        final int length;
        final long[] times, distances; // the numbers (milli)
        final long time, distance; // for part 2
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            String timeString = br.readLine().split(": +")[1];
            time = Long.parseLong(timeString.replace(" ", ""));
            String[] timeNumbers = timeString.split(" +");

            String distanceString = br.readLine().split(": +")[1];
            distance = Long.parseLong(distanceString.replace(" ", ""));
            String[] distanceNumbers = distanceString.split(" +");

            if (distanceNumbers.length != timeNumbers.length)
                throw new IllegalStateException();
            length = timeNumbers.length;
            times = new long[length];
            distances = new long[length];
            for (int i = 0; i < length; i++) {
                times[i] = Long.parseLong(timeNumbers[i]);
                distances[i] = Long.parseLong(distanceNumbers[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // part 1
        for (int i = 0; i < length; i++) {
            long result = 0;
            for (int j = 1; j < times[i]; j++)
                if (j * (times[i] - j) > distances[i])
                    result++;
            result1 *= result;
        }
        // part 2
        long margin = 0;
        for (long i = 1; i < time; i++)
            if (i * (time - i) > distance) {
                margin = i;
                break;
            }
        result2 = time - (margin * 2) + 1;

        return new Utils.PuzzlePair(result1, result2);
    }

    private Puzzle6() {
        throw new AssertionError("Cannot instantiate Puzzle6!");
    }
}
