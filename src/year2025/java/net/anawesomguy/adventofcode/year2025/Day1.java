package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.stream.Stream;

@AdventDay(day = 1)
public final class Day1 implements Puzzle.LineStreamed {
    private int[] input = new int[0];

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        input = lines.mapToInt(s -> s.charAt(0) == 'L' ?
                         -Integer.parseInt(s.substring(1)) :
                         Integer.parseInt(s.substring(1)))
                     .toArray();
    }

    @Override
    public long solvePart1() {
        int dial = 50;
        long result = 0;
        for (int i : input) {
            dial += i;
            if (dial < 0)
                do {
                    dial += 100;
                } while (dial < 0);
            else if (dial > 99)
                do {
                    dial -= 100;
                } while (dial > 99);

            if (dial == 0)
                result++;
        }
        return result;
    }

    @Override
    public long solvePart2() {
        int dial = 50;
        long result = 0;
        for (int i : input) {
            result += Math.abs(i / 100);
            i %= 100;
            if (dial == 0) {
                dial = i < 0 ? 100 + i : i;
            } else {
                dial += i;
                if (dial < 0) {
                    dial += 100;
                    result++;
                } else if (dial > 99) {
                    dial -= 100;
                    result++;
                } else if (dial == 0)
                    result++;
            }
        }
        return result;
    }
}
