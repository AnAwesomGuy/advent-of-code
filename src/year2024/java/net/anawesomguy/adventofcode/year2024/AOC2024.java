package net.anawesomguy.adventofcode.year2024;

import net.anawesomguy.adventofcode.AdventOfCode;

public final class AOC2024 {
    private AOC2024() {
        throw new AssertionError();
    }

    public static final int YEAR_NUM = 2024;

    public static void main(String... args) {
        AdventOfCode.addPuzzles(YEAR_NUM, Day1::new);
        AdventOfCode.solvePuzzle(YEAR_NUM, 1);
    }
}
