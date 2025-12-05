package net.anawesomguy.adventofcode.year2025;

import it.unimi.dsi.fastutil.longs.LongLongImmutablePair;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.Arrays;
import java.util.Comparator;

@AdventDay(day = 5)
public final class Day5 implements Puzzle.SingleLine {
    private LongLongPair[] ranges;
    private long[] available;

    @Override
    public void input(String string) throws InvalidInputException {
        String[] ss = string.split("\n\n");
        ranges = Arrays.stream(ss[0].split("\n")).map(s -> {
            String[] range = s.split("-");
            return new LongLongImmutablePair(Long.parseLong(range[0]), Long.parseLong(range[1]));
        }).sorted(Comparator.comparingLong(LongLongPair::firstLong)).toArray(LongLongPair[]::new);
        available = Arrays.stream(ss[1].split("\n")).mapToLong(Long::parseLong).toArray();
    }

    @Override
    public long solvePart1() {
        long result = 0;
        for (long i : available) {
            for (var pair : ranges)
                if (matches(pair, i)) {
                    result++;
                    break;
                }
        }
        return result;
    }

    public static boolean matches(LongLongPair range, long i) {
        return range.firstLong() <= i && range.secondLong() >= i;
    }

    @Override
    public long solvePart2() {
        long result = 0;
        long prevMax = 0;
        for (var pair : ranges) {
            long first = pair.firstLong(), second = pair.secondLong();
            if (second <= prevMax)
                continue;
            if (first <= prevMax)
                first = prevMax + 1;
            result += second - first + 1;
            prevMax = pair.secondLong();
        }
        return result;
    }
}
