package net.anawesomguy.adventofcode.year2024;

import static net.anawesomguy.adventofcode.AdventOfCode.addPuzzles;
import static net.anawesomguy.adventofcode.AdventOfCode.solvePuzzles;

public final class AOC2024 {
    private AOC2024() {
        throw new AssertionError();
    }

    public static final int YEAR_NUM = 2024;

    public static void main(String... args) {
        addPuzzles(YEAR_NUM,
                   Day1::new
        );
        solvePuzzles(YEAR_NUM);
    }
}
