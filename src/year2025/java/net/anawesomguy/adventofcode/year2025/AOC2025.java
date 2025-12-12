package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventOfCode;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2025.YEAR, puzzleClasses = {}, searchPackage = true)
public final class AOC2025 implements AdventOfCode.Annotated {
    public static final int YEAR = 2025;

    public static void main(String... args) {
        // AdventOfCode.main(args);
        // AdventOfCode.solveAndSubmit(Day10.class, true);
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
            [.....####.] (2,4,8) (0,1,2,3,4,7,8,9) (0,2,3,8,9) (0,1,2,4,6,7,8) (2,4,6,9) (0,3,5,6,8,9) (0,1,3,6,8) (0,1,2,6,7,8) (0,2,3,4,5,6,7,8) {95,57,91,68,66,24,71,53,109,62}
            """);
    }
}
