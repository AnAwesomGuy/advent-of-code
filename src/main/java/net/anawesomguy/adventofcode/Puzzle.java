package net.anawesomguy.adventofcode;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.function.Supplier;

public interface Puzzle {
    /**
     * @param input the puzzle input as a stream, will be closed after this method is called
     */
    void input(InputStream input) throws IOException, InvalidInputException;

    /**
     * Used to run common code between both parts. Can be assumed to be called before {@link #solvePart1()} and {@link #solvePart2()}.
     */
    default void init() {
    }

    interface WithBufferedReader extends Puzzle {
        default void input(InputStream input) throws IOException, InvalidInputException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                input(reader);
            }
        }

        /**
         * @param input the puzzle input as a reader, will be closed after this method is called
         */
        void input(BufferedReader input) throws IOException, InvalidInputException;
    }

    interface LineBased extends WithBufferedReader {
        @Override
        default void input(BufferedReader input) throws IOException, InvalidInputException {
            String str;
            while ((str = input.readLine()) != null)
                input(str);
        }

        /**
         * @param line a line of the puzzle input
         */
        void input(String line) throws InvalidInputException;
    }

    interface SingleLine extends Puzzle {
        @Override
        default void input(InputStream input) throws IOException, InvalidInputException {
            input(new String(input.readAllBytes()));
        }

        /**
         * @param singeLine the puzzles input, as a single line
         */
        void input(String singeLine) throws InvalidInputException;
    }

    interface LineListed extends WithBufferedReader {
        @Override
        default void input(BufferedReader input) throws UncheckedIOException, InvalidInputException {
            input(input.lines().toList());
        }

        /**
         * @param lines a list of the lines of the puzzles input
         */
        void input(List<String> lines) throws InvalidInputException;
    }

    /**
     * @return the puzzle answer for part one
     */
    int solvePart1();

    /**
     * @return the puzzle answer for part two
     */
    int solvePart2();

    interface PuzzleSupplier extends Supplier<Puzzle>, Comparable<PuzzleSupplier> {
        PuzzleSupplier FAIL = () -> { throw new UnsupportedOperationException(); };
        PuzzleSupplier EMPTY = () -> null;

        @Override
        default int compareTo(@NotNull Puzzle.PuzzleSupplier o) {
            return 0;
        }

        record Simple(int day, Supplier<Puzzle> supplier) implements PuzzleSupplier {
            @Override
            public Puzzle get() {
                return supplier.get();
            }

            @Override
            public int compareTo(@NotNull PuzzleSupplier other) {
                return other instanceof Simple ? Integer.compare(this.day, ((Simple)other).day) : 0;
            }
        }
    }
}
