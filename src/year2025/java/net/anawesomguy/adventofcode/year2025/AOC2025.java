package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventOfCode;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2025.YEAR, puzzleClasses = {}, searchPackage = true)
public final class AOC2025 implements AdventOfCode.Annotated {
    public static final int YEAR = 2025;

    public static void main(String... args) {
        // AdventOfCode.main(args);
        // AdventOfCode.solveAndSubmit(Day10.class, false);
        // AdventOfCode.getInputAndSolve(Day10.class);
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
        AdventOfCode.solvePuzzleWithInput(Day10.class, """
            [##...###.#] (0,1,5,6,7,8,9) (4,5) (1,2,3,5,6) (0,3) (8,9) (0,3,5,6,7,8,9) (0,1,4,6,7,9) (1,2) (5,8) {51,38,12,25,9,52,42,42,58,49}
            """);
    }
}
