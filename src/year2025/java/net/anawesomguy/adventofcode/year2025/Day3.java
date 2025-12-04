package net.anawesomguy.adventofcode.year2025;

import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.Arrays;
import java.util.stream.Stream;

@AdventDay(day = 3)
public final class Day3 implements Puzzle.LineStreamed {
    private char[][] lines;

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        this.lines = lines.map(String::toCharArray).toArray(char[][]::new);
    }

    @Override
    public long solvePart1() {
        return Arrays.stream(lines).mapToInt(chars -> {
            char c1 = '0', c2 = '0';
            int largestIndex = -1, len = chars.length;
            for (int i = 0; i < len; i++) {
                char c = chars[i];
                if (c > c1) {
                    c1 = c;
                    largestIndex = i;
                }
            }
            if (largestIndex == len - 1) {
                for (int i = 0; i < largestIndex; i++) {
                    char c = chars[i];
                    if (c >= c2)
                        c2 = c;
                }
                return ((c2 - '0') * 10) + (c1 - '0');
            }
            for (int i = largestIndex + 1; i < len; i++) {
                char c = chars[i];
                if (c >= c2)
                    c2 = c;
            }
            return ((c1 - '0') * 10) + (c2 - '0');
        }).sum();
    }

    @Override
    public long solvePart2() {
        return Arrays.stream(lines).mapToLong(chars -> {
            CharList charList = new CharArrayList(chars);
            long result = 0;
            int min = 0;
            for (int i = 11; i >= 0; i--) {
                int len = charList.size() - i;
                char max = 0;
                int maxIndex = -1;
                for (int j = min; j < len; j++) {
                    char c = charList.getChar(j);
                    if (c > max) {
                        maxIndex = j;
                        max = c;
                    }
                }
                result = result * 10 + (max - '0');
                charList.removeChar(min = maxIndex);
            }
            return result;
        }).sum();
    }
}
