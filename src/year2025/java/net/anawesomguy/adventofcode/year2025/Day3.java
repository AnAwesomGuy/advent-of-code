package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.Arrays;

@AdventDay(day = 3)
public final class Day3 implements Puzzle.SingleLine {
    private String[] lines;

    @Override
    public void input(String singleLine) throws InvalidInputException {
        this.lines = singleLine.split("\n");
    }

    @Override
    public long solvePart1() {
        return Arrays.stream(lines).mapToInt(str -> {
            char c1 = '0', c2 = '0';
            int largestIndex = -1, len = str.length();
            for (int i = 0; i < len; i++) {
                char c = str.charAt(i);
                if (c > c1) {
                    c1 = c;
                    largestIndex = i;
                }
            }
            if (largestIndex == len - 1) {
                for (int i = 0; i < largestIndex; i++) {
                    char c = str.charAt(i);
                    if (c >= c2)
                        c2 = c;
                }
                return ((c2 - '0') * 10) + (c1 - '0');
            }
            for (int i = largestIndex + 1; i < len; i++) {
                char c = str.charAt(i);
                if (c >= c2)
                    c2 = c;
            }
            return ((c1 - '0') * 10) + (c2 - '0');
        }).sum();
    }

    @Override
    public long solvePart2() {
        return Arrays.stream(lines).mapToLong(str -> {
            long result = 0;
            int min = 0;
            int i = 11;
            int upper = str.length() - i;
            for (; i >= 0; i--) {
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
        }).sum();
    }
}
