package net.anawesomguy.adventofcode.year2024;

import net.anawesomguy.adventofcode.AdventOfCode.Annotated;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2024.YEAR, puzzleClasses = {
    Day1.class, Day2.class
})
public final class AOC2024 implements Annotated {
    private AOC2024() {
        throw new AssertionError();
    }

    public static final int YEAR = 2024;
}
