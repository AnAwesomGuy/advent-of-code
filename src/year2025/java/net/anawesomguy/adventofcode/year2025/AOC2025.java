package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventOfCode;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2025.YEAR, puzzleClasses = {}, searchPackage = true)
public final class AOC2025 implements AdventOfCode.Annotated {
    public static final int YEAR = 2025;

    public static void main(String... args) {
        // AdventOfCode.main(args);
        AdventOfCode.solveAndSubmit(Day5.class, false);
        // AdventOfCode.getInputAndSolve(Day5.class);
        // AdventOfCode.solvePuzzleWithInput(Day5.class, """
        //     3-5
        //     10-14
        //     16-20
        //     12-18
        //
        //     1
        //     5
        //     8
        //     11
        //     17
        //     32
        //     """);
    }
}
