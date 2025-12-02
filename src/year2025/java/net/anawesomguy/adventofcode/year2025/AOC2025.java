package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventOfCode;
import net.anawesomguy.adventofcode.AdventOfCode.Annotated;
import net.anawesomguy.adventofcode.AdventYear;

@AdventYear(year = AOC2025.YEAR, puzzleClasses = {}, searchPackage = true)
public final class AOC2025 implements Annotated {
    public static final int YEAR = 2025;

    public static void main(String... args) {
        // AdventOfCode.main(args);
        // AdventOfCode.solveAndSubmit(Day2.class, false);
        AdventOfCode.getInputAndSolve(Day2.class);
        // AdventOfCode.solvePuzzleWithInput(Day2.class, "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124");
    }
}
