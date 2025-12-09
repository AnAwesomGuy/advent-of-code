package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventOfCode;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2025.YEAR, puzzleClasses = {}, searchPackage = true)
public final class AOC2025 implements AdventOfCode.Annotated {
    public static final int YEAR = 2025;

    public static void main(String... args) {
        // AdventOfCode.main(args);
        AdventOfCode.solveAndSubmit(Day8.class, false);
        // AdventOfCode.getInputAndSolve(Day8.class);
        // AdventOfCode.solvePuzzleWithInput(Day9.class, """
        //     7,1
        //     11,1
        //     11,7
        //     9,7
        //     9,5
        //     2,5
        //     2,3
        //     7,3
        //     """);
    }
}
