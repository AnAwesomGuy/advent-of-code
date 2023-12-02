package net.anawesomguy.adventofcode.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Utils {
    public static BufferedReader getReader(final URL inputURL) {
        try {
            final HttpURLConnection con = (HttpURLConnection)inputURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", "session=" + System.getenv("ADVENT_OF_CODE_SESSION"));
            return new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL newURL(String spec) {
        try {
            return new URL(spec);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record PuzzleIntPair(int firstHalf, int secondHalf) {}

    private Utils() {
        throw new AssertionError("Cannot instantiate Utils!");
    }
}
