package net.anawesomguy.adventofcode;

import net.anawesomguy.adventofcode.AdventOfCode.Submission;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Supplier;

public interface Puzzle {
    /**
     * @param input the puzzle input as a reader, will be closed after this method is called
     */
    void input(BufferedReader input) throws IOException, InvalidInputException;

    default void init() {
    }

    /**
     * @return the puzzle answer for part one
     * @see Submission#PART_ONE
     */
    int solvePart1();

    /**
     * @return the puzzle answer for part two
     * @see Submission#PART_TWO
     */
    int solvePart2();

    interface LineBased extends Puzzle {
        /**
         * @param line a line of the puzzle input
         */
        void input(String line) throws InvalidInputException;

        default void input(BufferedReader input) throws IOException {
            String str;
            while ((str = input.readLine()) != null)
                input(str);
        }
    }

    interface PuzzleSupplier extends Supplier<Puzzle>, Comparable<PuzzleSupplier> {
        @Override
        default int compareTo(@NotNull Puzzle.PuzzleSupplier o) {
            return 0;
        }
    }
}
