package net.anawesomguy.adventofcode.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Predicate;

public final class Utils {
    private static final String DEBUG_TEST_INPUT = """
                                                   """; // i can change this when debugging

    public static BufferedReader getReader(final URI inputURI) {
        try {
            //noinspection ConstantValue (ill change the value when i want)
            return new BufferedReader(
                DEBUG_TEST_INPUT == null || DEBUG_TEST_INPUT.isBlank() ? new InputStreamReader(
                    HttpClient.newHttpClient().send(
                        HttpRequest.newBuilder(inputURI)
                                   .headers("Cookie",
                                            "session=" + System.getenv("ADVENT_OF_CODE_SESSION"))
                                   .GET().build(),
                        HttpResponse.BodyHandlers.ofInputStream()
                    ).body()
                ) : new StringReader(DEBUG_TEST_INPUT)
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static URI newURI(final String spec) {
        try {
            return new URI(spec);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T log(T obj) {
        System.out.println(obj);
        return obj;
    }

    public static <T> boolean allMatch(List<T> list, Predicate<T> predicate) {
        boolean result = true;
        for (final T t : list) {
            result &= predicate.test(t);
        }
        return result;
    }

    public static long gcd(long a, long b) {
        if (a == b)
            return a;
        if (a == 0)
            return b;
        if (b == 0)
            return a;
        if ((~a & 1) == 1) {
            if ((b & 1) == 1)
                return gcd(a >> 1, b);
            return gcd(a >> 1, b >> 1) << 1;
        }
        if ((~b & 1) == 1)
            return gcd(a, b >> 1);
        if (a > b)
            return gcd((a - b) >> 1, b);
        return gcd((b - a) >> 1, a);
    }

    public static long lcm(long a, long b) {
        return a == 0 || b == 0 ? 0 : Math.abs(a * b) / gcd(a, b);
    }

    public static long lcm(final long... numbers) {
        long result = numbers[0];
        for (int i = 1; i < numbers.length; i++)
            result = lcm(result, numbers[i]);
        return result;
    }

    public record PuzzlePair(long firstHalf, long secondHalf) {}

    private Utils() {
        throw new AssertionError("Cannot instantiate Utils!");
    }
}
