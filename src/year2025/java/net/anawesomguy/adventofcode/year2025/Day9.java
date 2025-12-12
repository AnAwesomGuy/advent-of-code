package net.anawesomguy.adventofcode.year2025;

import it.unimi.dsi.fastutil.longs.LongLongImmutablePair;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.stream.Stream;

@AdventDay(day = 9)
public final class Day9 implements Puzzle.LineStreamed {
    private LongLongPair[] locations;

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        locations = lines.map(s -> {
            String[] split = s.split(",");
            return new LongLongImmutablePair(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }).toArray(LongLongPair[]::new);
    }

    @Override
    public long solvePart1() {
        long largest = 0;
        for (int i = 0, len = locations.length; i < len; i++) {
            var pos1 = locations[i];
            for (int j = i + 1; j < len; j++) {
                var pos2 = locations[j];
                long size = Math.abs((pos1.firstLong() - pos2.firstLong() + 1) *
                                         (pos1.secondLong() - pos2.secondLong() + 1));
                if (size > largest)
                    largest = size;
            }
        }
        return largest;
    }

    @Override
    public long solvePart2() {
        return 0;
    }
}
