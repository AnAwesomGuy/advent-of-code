package net.anawesomguy.adventofcode;

import net.anawesomguy.adventofcode.puzzles.*;

public final class Main {
    public static void main(final String... args) {
        Puzzle1.main();
        System.out.println();
    }

    private Main() {
        throw new AssertionError("Cannot instantiate Main!");
    }
}
