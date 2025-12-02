package net.anawesomguy.adventofcode.year2025;

import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.Arrays;
import java.util.regex.Pattern;

@AdventDay(day = 2)
public class Day2 implements Puzzle.SingleLine {
    private LongLongPair[] ids;

    @Override
    public void input(String singleLine) throws InvalidInputException {
        ids = Arrays.stream(singleLine.split(",")).filter(s -> !s.startsWith("0") && !s.contains("-0")).map(s -> {
            String[] ss = s.split("-");
            return LongLongPair.of(Long.parseLong(ss[0]), Long.parseLong(ss[1].trim()));
        }).toArray(LongLongPair[]::new);
    }

    @Override
    public long solvePart1() {
        return Arrays.stream(ids)
                     .mapToLong(p -> {
                         long l = 0;
                         for (long i1 = p.firstLong(), i2 = p.secondLong(); i1 <= i2; i1++) {
                             int digits = ((int)Math.log10(i1) + 1) / 2;
                             int tenP = 10;
                             while (digits-- > 0) {
                                 if (i1 % tenP == i1 / tenP)
                                     l += i1;
                                 tenP *= 10;
                             }
                         }
                         return l;
                     }).sum();
    }

    @Override
    public long solvePart2() {
        Pattern pattern = Pattern.compile("(\\d+)\\1+");
        return Arrays.stream(ids)
                     .mapToLong(p -> {
                         long l = 0;
                         for (long i1 = p.firstLong(), i2 = p.secondLong(); i1 <= i2; i1++)
                             if (pattern.matcher(Long.toUnsignedString(i1)).matches())
                                 l += i1;
                         return l;
                     }).sum();
    }
}
