package net.anawesomguy.adventofcode.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Utils {
    public static BufferedReader getReader(final URI inputURI) {
        try {
            return new BufferedReader(
                new InputStreamReader(
                    HttpClient.newHttpClient().send(
                        HttpRequest.newBuilder(inputURI)
                                   .headers("Cookie",
                                            "session=" + System.getenv("ADVENT_OF_CODE_SESSION"))
                                   .GET().build(),
                        HttpResponse.BodyHandlers.ofInputStream()
                    ).body()
                )
            );
        } catch (IOException | InterruptedException e) {
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

    public record PuzzleIntPair(int firstHalf, int secondHalf) {}

    private Utils() {
        throw new AssertionError("Cannot instantiate Utils!");
    }
}
