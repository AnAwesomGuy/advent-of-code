package net.anawesomguy.adventofcode.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Utils {
    private static final String DEBUG_TEST_INPUT = null; // i can change this when debugging

    public static BufferedReader getReader(final URI inputURI) {
        try {
            //noinspection ConstantValue (ill change the value when i want)
            return new BufferedReader(
                DEBUG_TEST_INPUT == null ? new InputStreamReader(
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

    public record PuzzlePair(long firstHalf, long secondHalf) {}

    private Utils() {
        throw new AssertionError("Cannot instantiate Utils!");
    }
}
