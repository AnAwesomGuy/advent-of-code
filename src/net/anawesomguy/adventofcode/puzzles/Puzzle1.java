package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Puzzle1 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/1/input");
    private static final Map<String, String> WORDS_TO_DIGITS = Map.of(
        "one", "1",
        "two", "2",
        "three", "3",
        "four", "4",
        "five", "5",
        "six", "6",
        "seven", "7",
        "eight", "8",
        "nine", "9"
    );

    public static void main(final String... args) {
        Utils.PuzzleIntPair answer = solve();
        System.out.println("Answer for first half of puzzle 1: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 1: " + answer.secondHalf());
    }

    public static Utils.PuzzleIntPair solve() {
        int result1 = 0, result2 = 0; // result1 = first half, result2 = second half
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                result1 += firstLastDigits(inputLine);
                result2 += firstLastDigits(wordsToDigits(inputLine));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Utils.PuzzleIntPair(result1, result2);
    }

    /**
     * Returns the first and last digits in a {@code String}.<br>
     * This does not account for decimals and minus signs.<br>
     * If the {@code String} contains no numbers, a {@link NumberFormatException} is thrown.
     * <p>Examples:
     * <blockquote><pre>
     *     firstLastDigits("uqiw9iq1q") returns 91
     *     firstLastDigits("42183") returns 43
     *     firstLastDigits("ao2mfj1ua8") returns 28
     *     firstLastDigits("jie3ijiq") returns 33
     *     firstLastDigits("iowej") throws {@link NumberFormatException}
     * </pre></blockquote>
     *
     * @param input the {@code String} to get the first and last digits of.
     * @return the first and last digits in {@code input}.
     * @throws NumberFormatException if there are no digits in {@code input}.
     */
    public static int firstLastDigits(String input) throws NumberFormatException {
        StringBuilder builder = new StringBuilder();
        // iterate over the input's chars
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // check if it is a number and add to builder
            if (c >= '0' && c <= '9')
                builder.append(c);
        }
        String str = builder.toString();
        try {
            // return first digit * 10 + second digit
            return (((str.charAt(0) - '0') * 10) + (str.charAt(str.length() - 1) - '0'));
        } catch (IndexOutOfBoundsException e) {
            throw new NumberFormatException("Input \"" + input + "\" contains no digits!");
        }
    }

    public static String wordsToDigits(String input) {
        // replace all the word numbers with digits
        final Map<Integer, String> map = new LinkedHashMap<>(4);
        for (final Map.Entry<String, String> entry : WORDS_TO_DIGITS.entrySet()) {
            int firstIndex = input.indexOf(entry.getKey());
            int lastIndex = input.lastIndexOf(entry.getKey());
            if (firstIndex != -1)
                map.put(firstIndex, entry.getValue());
            if (lastIndex != -1)
                map.put(lastIndex, entry.getValue());
        }
        final StringBuilder builder = new StringBuilder(input.length());
        final List<Integer> intList = map.keySet().stream().sorted((o1, o2) -> o1 < o2 ? o2 : o1).toList(); // will not have duplicate values
        // iterate over input
        for (int i = 0; i < input.length(); i++) {
            final String str = map.get(i);
            if (str != null) {
                builder.append(str);
                int j = intList.get(intList.indexOf(i));
                if (j < (i += str.length() - 1))
                    i = j;
            } else
                builder.append(input.charAt(i));
        }
        return builder.toString();
    }

    private Puzzle1() {
        throw new AssertionError("Cannot instantiate Puzzle1!");
    }
}