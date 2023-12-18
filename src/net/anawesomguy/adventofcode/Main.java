package net.anawesomguy.adventofcode;

import net.anawesomguy.adventofcode.puzzles.*;

public final class Main {
    public static void main(final String... args) {
        Puzzle1.main();
        System.out.println();
        Puzzle2.main();
        System.out.println();
        // puzzle 3 is in js, see the javascript folder in root (its unfinished)
        Puzzle4.main();
        System.out.println();
        Puzzle5.main();
        System.out.println();
        Puzzle6.main();
        System.out.println();
        Puzzle7.main();
        System.out.println();
    }

    private Main() {
        throw new AssertionError("Cannot instantiate Main!");
    }
}
