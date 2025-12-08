package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

@AdventDay(day = 3)
public final class Day3 implements Puzzle.SingleLine {
    private String[] lines;

    @Override
    public void input(String singleLine) throws InvalidInputException {
        this.lines = singleLine.split("\n");
    }

    @Override
    public long solvePart1() {
        long sum = 0L;
        for (String str : lines)
            sum += largestJoltage(str, 2);
        return sum;
    }

    @Override
    public long solvePart2() {
        long sum = 0L;
        for (String str : lines)
            sum += largestJoltage(str, 12);
        return sum;
    }

    public static long largestJoltage(String str, int len) {
        long result = 0;
        for (int upper = str.length() - --len, min = 0; len >= 0; len--) {
            char max = 0;
            int maxIndex = -1;
            for (int j = min; j < upper; j++) {
                char c = str.charAt(j);
                if (c > max) {
                    maxIndex = j;
                    max = c;
                }
            }
            result = result * 10 + (max - '0');
            min = maxIndex + 1;
            upper++;
        }
        return result;
    }
}
