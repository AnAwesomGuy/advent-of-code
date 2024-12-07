package net.anawesomguy.adventofcode;

import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.anawesomguy.adventofcode.Puzzle.PuzzleSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public final class AdventOfCode {
    private AdventOfCode() {
        throw new AssertionError();
    }

    public static final URI AOC_URI = URI.create("https://adventofcode.com/");
    private static final Comparator<PuzzleSupplier> NULLS_LAST = Comparator.nullsLast(Comparator.naturalOrder());

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
        int length = puzzles.length;
        if (length == 0)
            return;
        if (length > 25)
            throw new IllegalArgumentException("cannot have more than 25 puzzles per year!");
        PuzzleSupplier[] oldPuzzles = PUZZLES_BY_YEAR.get(year);
        if (oldPuzzles == null) {
            if (length != 25)
                puzzles = Arrays.copyOf(puzzles, 25);
            Arrays.sort(puzzles, NULLS_LAST);
            PUZZLES_BY_YEAR.put(year, puzzles);
        } else {
            if (oldPuzzles.length != 25)
                throw new AssertionError();
            for (int i = 0, j = 0; i < 25/*oldPuzzles.length*/; i++)
                if (oldPuzzles[i] == null)
                    oldPuzzles[i] = puzzles[j++];
            Arrays.sort(oldPuzzles, NULLS_LAST);
            // if (j != length) // puzzles wasnt fully fitted into oldPuzzles
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
        try (InputStream input = client.send(
                                           HttpRequest.newBuilder(AOC_URI.resolve(String.format("%s/day/%s/input", year, day)))
                                                      .GET().build(),
                                           BodyHandlers.ofInputStream())
                                       .body()
        ) {
            System.out.printf("Solving: Day %s, Year %s%n", day, year);

            long before = System.nanoTime();
            Puzzle puzzle = supplier.get();
            puzzle.input(input);
            puzzle.init();
            double timeElapsed = (System.nanoTime() - before) / 1e6;
            System.out.printf("Puzzle input supplied and initiated in %.3f ms!%n", timeElapsed);

            long before1 = System.nanoTime();
            int result1 = puzzle.solvePart1();
            double timeElapsed1 = (System.nanoTime() - before1) / 1e6;
            System.out.printf("Part one solved in %.3f ms!%n" +
                              "Result: %s%n", timeElapsed1, result1);

            long before2 = System.nanoTime();
            int result2 = puzzle.solvePart2();
            double timeElapsed2 = (System.nanoTime() - before2) / 1e6;
            System.out.printf("Part two solved in %.3f ms!%n" +
                              "Result: %s%n" +
                              "Total time for solve: %.3f ms%n",
                              timeElapsed2, result2, (timeElapsed + timeElapsed1 + timeElapsed2));
        } catch (Exception e) {
            System.out.printf("Exception occurred while solving puzzle for day %s of %s!%n", day, year);
            e.printStackTrace();
        } finally {
            System.out.println();
        }
    }

    public static HttpClient createHttpClient() {
        return HttpClient.newBuilder().cookieHandler(CookieHandler.getDefault()).build();
    }
}