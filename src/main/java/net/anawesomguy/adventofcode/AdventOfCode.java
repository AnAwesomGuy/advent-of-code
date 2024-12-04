package net.anawesomguy.adventofcode;

import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.anawesomguy.adventofcode.Puzzle.PuzzleSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Objects;

public final class AdventOfCode {
    private AdventOfCode() {
        throw new AssertionError();
    }

    public static final URI AOC_URI = URI.create("https://adventofcode.com/");

    static {
        //session cookie (aoc needs authentication)
        CookieManager cookies = new CookieManager();
        CookieManager.setDefault(cookies);
        HttpCookie sessionCookie = new HttpCookie("session", System.getenv("AOC_SESSION"));
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);
        cookies.getCookieStore().add(AOC_URI, sessionCookie);
    }

    private static final Int2ObjectSortedMap<@NotNull PuzzleSupplier[]>
        PUZZLES_BY_YEAR = new Int2ObjectRBTreeMap<>(IntComparators.OPPOSITE_COMPARATOR);

    public static void addPuzzles(int year, @NotNull PuzzleSupplier... puzzles) {
        if (puzzles == null)
            throw new NullPointerException("tried to register null puzzles!");
        if (puzzles.length == 0)
            return;
        if (puzzles.length > 25)
            throw new IllegalArgumentException("cannot have more than 25 puzzles per year!");
        PuzzleSupplier[] oldPuzzles = PUZZLES_BY_YEAR.get(year);
        if (oldPuzzles == null) {
            if (puzzles.length != 25)
                puzzles = Arrays.copyOf(puzzles, 25);
            PUZZLES_BY_YEAR.put(year, puzzles);
        } else {
            if (oldPuzzles.length == 25)
                throw new AssertionError();
            int j = 0;
            for (int i = 0; i < oldPuzzles.length; i++)
                if (oldPuzzles[i] == null)
                    oldPuzzles[i] = puzzles[j++];
            //noinspection StatementWithEmptyBody
            if (j != puzzles.length) {
                // do something? idk puzzles wasnt fully fitted into the oldPuzzles
            }
        }
    }

    public static void solvePuzzles(int year) {
        PuzzleSupplier[] puzzles = PUZZLES_BY_YEAR.get(year);
        if (puzzles == null)
            throw new IllegalArgumentException();

        try (HttpClient client = createHttpClient()) {
            for (int day = 0; day < puzzles.length; day++) {
                PuzzleSupplier puzzle = puzzles[day];
                if (puzzle != null)
                    getInputAndSolve(year, day + 1, puzzles[day], client);
            }
        }
    }

    public static void solvePuzzle(int year, int day) {
        if (day > 25 || day < 1)
            throw new IllegalArgumentException("day is out of range");
        PuzzleSupplier[] puzzles = PUZZLES_BY_YEAR.get(year);
        if (puzzles == null)
            throw new IllegalArgumentException();

        try (HttpClient client = createHttpClient()) {
            getInputAndSolve(year, day,
                             Objects.requireNonNull(puzzles[day - 1], () -> String.format(
                                 "no solution has been added for year %s, day %s", year, day)),
                             client);
        }
    }

    public static void getInputAndSolve(int year, int day, @NotNull PuzzleSupplier supplier,
                                        @NotNull HttpClient client) {
        System.out.println();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(client.send(
                                            HttpRequest.newBuilder(AOC_URI.resolve(String.format("%s/day/%s/input", year, day)))
                                                       .GET().build(),
                                            BodyHandlers.ofInputStream())
                                        .body()))
        ) {
            System.out.printf("Solving: Day %s, Year %s%n", day, year);

            long before = System.nanoTime();
            Puzzle puzzle = supplier.get();
            puzzle.input(reader);
            puzzle.init();
            long timeElapsed = System.nanoTime() - before;
            System.out.printf("Puzzle input supplied and initiated in %s seconds!%n", timeElapsed / 1e9);

            long before1 = System.nanoTime();
            int result1 = puzzle.solvePart1();
            long timeElapsed1 = System.nanoTime() - before1;
            System.out.printf("Part one solved in %s seconds!%n" +
                              "Result: %s%n", timeElapsed1 / 1e9, result1);

            long before2 = System.nanoTime();
            int result2 = puzzle.solvePart2();
            long timeElapsed2 = System.nanoTime() - before2;
            System.out.printf("Part two solved in %s seconds!%n" +
                              "Result: %s%n" +
                              "Total time: %s%n",
                              timeElapsed2 / 1e9, result2, (timeElapsed + timeElapsed1 + timeElapsed2));

        } catch (Exception e) {
            System.out.printf("Exception occurred while solving puzzle for day %s of %s!%n", day, year);
            e.printStackTrace();
        }
    }

    public static HttpClient createHttpClient() {
        return HttpClient.newBuilder().cookieHandler(CookieHandler.getDefault()).build();
    }
}