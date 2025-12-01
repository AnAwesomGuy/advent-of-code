package net.anawesomguy.adventofcode;

import com.google.common.base.Suppliers;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.anawesomguy.adventofcode.Puzzle.PuzzleSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Supplier;

import static net.anawesomguy.adventofcode.AOCStatics.EMPTY_PUZZLES;
import static net.anawesomguy.adventofcode.AOCStatics.NULLS_LAST;
import static net.anawesomguy.adventofcode.AOCStatics.PUZZLES_BY_YEAR;
import static net.anawesomguy.adventofcode.AOCStatics.WALKER;

public interface AdventOfCode {
    // instance methods

    int getYear();

    PuzzleSupplier[] getPuzzles();

    interface Annotated extends AdventOfCode {
        @Override
        default int getYear() {
            Class<? extends AdventOfCode> clazz = this.getClass();
            AdventYear adventYear = this.getClass().getAnnotation(AdventYear.class);
            if (adventYear == null) {
                AdventYear packageYear = clazz.getPackage().getAnnotation(AdventYear.class);
                if (packageYear == null) // TODO replace with better exception
                    throw new NoSuchElementException("class extends " + this.getClass().getName() +
                                                         " but neither it nor its package is annotated with @" +
                                                         AdventYear.class.getName() + "!");
                return packageYear.year();
            }
            return adventYear.year();
        }

        @Override
        default PuzzleSupplier[] getPuzzles() {
            Class<? extends Puzzle>[] puzzleClasses = getPuzzleClasses();
            int length;
            if (puzzleClasses == null || (length = puzzleClasses.length) == 0)
                return EMPTY_PUZZLES;
            if (length > 25)
                System.err.println(
                    this.getClass().getName() + "%s#getPuzzleClasses returned an array larger than 25, truncating!");
            PuzzleSupplier[] puzzles = new PuzzleSupplier[length];
            for (int i = 0; i < length; i++) {
                Class<? extends Puzzle> clazz = puzzleClasses[i];
                if (clazz == null)
                    continue;
                puzzles[i] = PuzzleSupplier.from(clazz);
            }
            return puzzles;
        }

        @SuppressWarnings("unchecked")
        default Class<? extends Puzzle>[] getPuzzleClasses() {
            Class<? extends AdventOfCode> clazz = this.getClass();
            AdventYear adventYear = this.getClass().getAnnotation(AdventYear.class);
            Class<? extends Puzzle>[] puzzles;
            if (adventYear == null) {
                AdventYear packageYear = clazz.getPackage().getAnnotation(AdventYear.class);
                if (packageYear == null)
                    throw new NoSuchElementException("class extends " + AdventOfCode.class.getName() +
                                                         " but neither it nor its package is annotated with @" +
                                                         AdventYear.class.getName() + "!");
                puzzles = packageYear.puzzleClasses();
            } else {
                if (adventYear.searchPackage()) {
                    ArrayList<Class<? extends Puzzle>> puzzlesList = new ArrayList<>();
                    String packageName = clazz.getPackageName();
                    try {
                        for (ClassInfo info : ClassPath.from(clazz.getClassLoader()).getTopLevelClasses(packageName)) {
                            Class<?> puzzleClass = info.load();
                            if (Puzzle.class.isAssignableFrom(puzzleClass))
                                puzzlesList.add((Class<? extends Puzzle>)puzzleClass);
                        }
                    } catch (IOException e) {
                        System.err.printf("Error scanning package %s!%n", packageName);
                    }
                    puzzlesList.addAll(Arrays.asList(adventYear.puzzleClasses()));
                    puzzles = puzzlesList.toArray(new Class[0]);
                } else
                    puzzles = adventYear.puzzleClasses();
            }
            return puzzles;
        }
    }

    // main method

    static void main(String... args) {
        Iterator<AdventOfCode> iterator = ServiceLoader.load(AdventOfCode.class).iterator();
        while (iterator.hasNext()) {
            try {
                AdventOfCode aoc = iterator.next();
                addPuzzles(aoc.getYear(), aoc.getPuzzles());
            } catch (Exception e) {
                System.err.println("Error loading service!");
                e.printStackTrace();
            }
        }

        long before = System.nanoTime();
        solveAllPuzzles();
        System.out.printf("%nSolved all puzzles in %s ms.%n%n", (System.nanoTime() - before) / 1e6);
    }

    // static methods for getting and registering puzzles

    URI AOC_URI = URI.create("https://adventofcode.com/");
    Path CACHE_PATH = Path.of(".aoc_cache");
    // DON'T CLOSE
    Supplier<HttpClient> LAZY_HTTP_CLIENT = Suppliers.memoize(
        () -> HttpClient.newBuilder().cookieHandler(CookieHandler.getDefault()).build());

    @NotNull
    static PuzzleSupplier[] getPuzzles(int year) {
        PuzzleSupplier[] puzzles = PUZZLES_BY_YEAR.get(year);
        if (puzzles == null)
            return EMPTY_PUZZLES;
        int length = puzzles.length, empty = 0;
        for (int i = length - 1; i >= 0; i--)
            if (puzzles[i] == null)
                empty++;
            else
                break;
        return Arrays.copyOf(puzzles, length - empty); // makes a COPY that removes the null elements at the end
    }

    static PuzzleSupplier getPuzzle(int year, @Range(from = 1, to = 25) int day) {
        if (day > 25 || day < 1)
            throw new IllegalArgumentException("cannot have more than 25 or less than 1 advent days per year!");
        PuzzleSupplier[] puzzles = PUZZLES_BY_YEAR.get(year);
        if (puzzles != null)
            return puzzles[day];
        return null;
    }

    static void addPuzzle(int year, @Range(from = 1, to = 25) int day, @NotNull PuzzleSupplier puzzle) {
        if (puzzle == null)
            throw new NullPointerException("tried to add null puzzle!");
        if (day > 25 || day < 1)
            throw new IllegalArgumentException("cannot have more than 25 or less than 1 advent days per year!");
        PuzzleSupplier[] oldPuzzles = PUZZLES_BY_YEAR.get(year);
        if (oldPuzzles == null) {
            oldPuzzles = new PuzzleSupplier[25];
            oldPuzzles[day] = puzzle;
            PUZZLES_BY_YEAR.put(year, oldPuzzles);
        } else
            oldPuzzles[day] = puzzle;
    }

    static void addPuzzles(int year, PuzzleSupplier @NotNull ... puzzles) {
        if (puzzles == null)
            throw new NullPointerException("tried to add null puzzles!");
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
            assert oldPuzzles.length == 25;
            for (int i = 0, j = 0; i < 25/*oldPuzzles.length*/; i++)
                if (oldPuzzles[i] == null)
                    oldPuzzles[i] = puzzles[j++];
            Arrays.sort(oldPuzzles, NULLS_LAST);
            // if (j != length) // puzzles wasn't fully fitted into oldPuzzles
        }
    }

    // static methods to solve puzzles

    static void solveAllPuzzles() {
        for (Entry<PuzzleSupplier[]> entry : PUZZLES_BY_YEAR.int2ObjectEntrySet()) {
            int year = entry.getIntKey();
            System.out.println("Solving puzzles for year " + year);
            System.out.println("----------------------------------------------------------------");
            System.out.println();
            PuzzleSupplier[] puzzles = entry.getValue();
            for (int day = 0; day < puzzles.length; day++) {
                PuzzleSupplier puzzle = puzzles[day];
                if (puzzle != null)
                    getInputAndSolve(year, day + 1, puzzles[day]);
            }
        }
    }

    static void solvePuzzles(int year) {
        PuzzleSupplier[] puzzles = PUZZLES_BY_YEAR.get(year);
        if (puzzles == null)
            throw new IllegalArgumentException();

        for (int day = 0; day < puzzles.length; day++) {
            PuzzleSupplier puzzle = puzzles[day];
            if (puzzle != null && puzzle != PuzzleSupplier.EMPTY)
                getInputAndSolve(year, day + 1, puzzles[day]);
        }
    }

    static void solvePuzzle(int year, @Range(from = 1, to = 25) int day) {
        if (day > 25 || day < 1)
            throw new IllegalArgumentException("day is out of range");
        PuzzleSupplier[] puzzles = PUZZLES_BY_YEAR.get(year);
        if (puzzles == null)
            throw new IllegalArgumentException();

        getInputAndSolve(year, day, Objects.requireNonNull(puzzles[day - 1], () -> String.format(
            "no solution has been added for year %s, day %s", year, day)));
    }

    static InputStream getInput(int year, @Range(from = 1, to = 25) int day) throws IOException {
        String inputPath = String.format("%s/day/%s/input", year, day);
        Path path = CACHE_PATH.resolve(inputPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            try {
                path = LAZY_HTTP_CLIENT.get()
                                       .send(
                                           HttpRequest.newBuilder(AOC_URI.resolve(inputPath))
                                                      .GET().build(),
                                           BodyHandlers.ofFile(path))
                                       .body();
            } catch (InterruptedException e) {
                throw new IOException("sending request to /" + inputPath, e);
            }
        }
        return Files.newInputStream(path);
    }

    static void getInputAndSolve(int year, @Range(from = 1, to = 25) int day, @NotNull PuzzleSupplier supplier) {
        try {
            solvePuzzleWithInput(year, day, supplier, getInput(year, day));
        } catch (Exception e) {
            System.err.printf("Exception occurred while solving puzzle for day %s of %s!%n", day, year);
            e.printStackTrace();
        } finally {
            System.out.println();
        }
    }

    static void solvePuzzleWithInput(int year, @Range(from = 1, to = 25) int day,
                                     @NotNull PuzzleSupplier supplier, @NotNull InputStream in) {
        System.out.printf("Solving: Day %s, Year %s.%n", day, year);

        long before = System.nanoTime();
        Puzzle puzzle = supplier.get();
        if (puzzle == null) {
            System.out.println("Got null puzzle, cancelling!");
            return;
        }
        try (in) {
            puzzle.input(in);
        } catch (IOException e) {
            System.err.println("Exception occurred during puzzle input read, cancelling!");
            e.printStackTrace();
            return;
        }
        puzzle.init();
        double timeElapsed = (System.nanoTime() - before) / 1e6; // ms
        System.out.printf("Puzzle input supplied and initiated in %.3f ms.%n", timeElapsed);

        long before1 = System.nanoTime();
        long result1 = puzzle.solvePart1();
        if (result1 < 0) {
            System.err.printf("Got negative solution %s for part one!", result1);
            return;
        }
        double timeElapsed1 = (System.nanoTime() - before1) / 1e6; // ms
        System.out.printf("Part one solved in %.3f ms!%n" +
                              "Result: %s%n", timeElapsed1, result1);

        long before2 = System.nanoTime();
        long result2 = puzzle.solvePart2();
        double timeElapsed2 = (System.nanoTime() - before2) / 1e6; // ms
        if (result2 < 0) {
            System.err.printf("Got negative solution %s for part two!", result2);
            return;
        }
        System.out.printf("Part two solved in %.3f ms!%n" +
                              "Result: %s%n" +
                              "Total time for solve: %.3f ms.%n",
                          timeElapsed2, result2, (timeElapsed + timeElapsed1 + timeElapsed2));
    }

    // methods to solve and submit

    static void solveAndSubmit(Class<? extends Puzzle> clazz, boolean part1) {
        Class<?> calling = WALKER.getCallerClass();
        int year;
        AdventYear adventYear = calling.getAnnotation(AdventYear.class);
        if (adventYear == null) {
            AdventYear packageYear = clazz.getPackage().getAnnotation(AdventYear.class);
            if (packageYear == null)
                throw new NoSuchElementException(
                    "class calling solveAndSubmit has no advent year information! annotate the class with @" + AdventYear.class.getName());
            year = packageYear.year();
        } else
            year = adventYear.year();
        solveAndSubmit(year, PuzzleSupplier.from(clazz), part1);
    }

    static void solveAndSubmit(int year, PuzzleSupplier supplier, boolean part1) {
        int day = supplier.day();
        String partName = part1 ? "one" : "two";
        System.out.printf("Solving and submitting: Day %s Part %s, Year %s.%n", day, year, partName);

        long before = System.nanoTime();
        Puzzle puzzle = supplier.get();
        if (puzzle == null) {
            System.out.println("Got null puzzle, cancelling!");
            return;
        }
        try (InputStream in = getInput(year, day)) {
            puzzle.input(in);
        } catch (IOException e) {
            System.err.println("Exception occurred during puzzle input read, cancelling!");
            e.printStackTrace();
            return;
        }
        puzzle.init();
        System.out.printf("Puzzle input supplied and initiated in %.3f ms.%n", (System.nanoTime() - before) / 1e6);

        long beforeSolve = System.nanoTime();
        long result = part1 ? puzzle.solvePart1() : puzzle.solvePart2();
        if (result < 0) {
            System.err.printf("Got negative solution %s for part %s!2n", result, partName);
            return;
        }
        System.out.printf("Part %s solved in %.3f ms!%n" +
                              "Result: %s%n", partName, (System.nanoTime() - beforeSolve) / 1e6, result);

        URI answerUri = AOC_URI.resolve(String.format("%s/day/%s/answer", year, day));
        try {
            HttpResponse<String> response =
                LAZY_HTTP_CLIENT.get()
                                .send(HttpRequest.newBuilder(answerUri)
                                                 .POST(HttpRequest.BodyPublishers.ofString(
                                                     "level=" + (part1 ? 1 : 2) + "&answer=" + result))
                                                 .build(),
                                      BodyHandlers.ofString());
            if (response.statusCode() != 200)
                System.err.printf("Submission returned failed status code %s!%n", response.statusCode());
            System.out.println("Got submission response: " +
                                   response.body()
                                           .replaceAll("(?s).*?<article[^>]*>(.*?)</article>.*", "$1")
                                           .replaceAll("<[^>]*>", ""));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Contains all the static fields and methods that shouldn't be exposed because interfaces cannot have private members.
 */
final class AOCStatics {
    private AOCStatics() {
        throw new AssertionError();
    }

    static final Comparator<PuzzleSupplier> NULLS_LAST = Comparator.nullsLast(Comparator.naturalOrder());
    static final PuzzleSupplier[] EMPTY_PUZZLES = new PuzzleSupplier[0];
    static final Int2ObjectSortedMap<@NotNull PuzzleSupplier[]>
        PUZZLES_BY_YEAR = new Int2ObjectRBTreeMap<>(IntComparators.OPPOSITE_COMPARATOR);
    static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    static {
        // session cookie (aoc needs authentication)
        CookieManager cookies = new CookieManager();
        CookieManager.setDefault(cookies);
        HttpCookie sessionCookie = new HttpCookie("session", System.getenv("AOC_SESSION"));
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);
        cookies.getCookieStore().add(AdventOfCode.AOC_URI, sessionCookie);
    }
}