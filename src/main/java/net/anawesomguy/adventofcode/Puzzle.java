package net.anawesomguy.adventofcode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
         * @param singleLine the puzzles input, as a single line
         */
        void input(String singleLine) throws InvalidInputException;
    }

    interface LineStreamed extends WithBufferedReader {
        @Override
        default void input(BufferedReader input) throws UncheckedIOException, InvalidInputException {
            try (Stream<String> lines = input.lines().onClose(() -> {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })) {
                input(lines);
            }
        }

        /**
         * @param lines a list of the lines of the puzzles input
         */
        void input(Stream<String> lines) throws InvalidInputException;
    }

    /**
     * @return the puzzle answer for part one
     */
    @Range(from = 0, to = Long.MAX_VALUE)
    long solvePart1();

    /**
     * @return the puzzle answer for part two
     */
    @Range(from = 0, to = Long.MAX_VALUE)
    long solvePart2();

    record PuzzleSupplier(int day, Supplier<Puzzle> supplier) implements Supplier<Puzzle>, Comparable<PuzzleSupplier> {
        public static final PuzzleSupplier EMPTY = unordered(() -> null);

        public static PuzzleSupplier unordered(Supplier<Puzzle> supplier) {
            return new PuzzleSupplier(-1, supplier);
        }

        public static PuzzleSupplier from(Class<? extends Puzzle> clazz) {
            AdventDay adventDay = clazz.getAnnotation(AdventDay.class);
            return adventDay == null ?
                unordered(() -> instantiatePuzzle(clazz)) :
                new PuzzleSupplier(adventDay.day(), () -> instantiatePuzzle(clazz));
        }

        static Puzzle instantiatePuzzle(Class<? extends Puzzle> clazz) {
            try {
                Constructor<? extends Puzzle> constructor = clazz.getDeclaredConstructor();
                constructor.trySetAccessible();
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                System.err.printf("Puzzle %s does not have a no-args constructor!%n", clazz.getName());
                return null;
            } catch (ReflectiveOperationException | SecurityException e) {
                System.err.printf("Error instantiating puzzle %s!%n", clazz.getName());
                return null;
            }
        }

        @Override
        public Puzzle get() {
            return supplier.get();
        }

        @Override
        public int compareTo(@NotNull PuzzleSupplier other) {
            return day > 0 ? Integer.compare(this.day, other.day) : 1; // unordered last
        }
    }
}
