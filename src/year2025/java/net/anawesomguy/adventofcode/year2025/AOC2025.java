package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventOfCode;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2025.YEAR, puzzleClasses = {}, searchPackage = true)
public final class AOC2025 implements AdventOfCode.Annotated {
    public static final int YEAR = 2025;

    public static void main(String... args) {
        // AdventOfCode.main(args);
        AdventOfCode.solveAndSubmit(Day6.class, false);
        // AdventOfCode.getInputAndSolve(Day6.class);
        // AdventOfCode.solvePuzzleWithInput(Day6.class, """
        //     123 328  51 64\s
        //      45 64  387 23\s
        //       6 98  215 314
        //     *   +   *   + \s
        //     """);
    }
}
