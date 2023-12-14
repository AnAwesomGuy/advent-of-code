package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Puzzle7 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/7/input");
    private static final char[] CARDS1 = {'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'};
    private static final char[] CARDS2 = {'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'}; // joker

    public static void main(final String... args) {
        Utils.PuzzlePair answer = solve();
        System.out.println("Answer for first half of puzzle 7: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 7: " + answer.secondHalf());
    }

    public static Utils.PuzzlePair solve() {
        long result1 = 0, result2 = 0;
        List<Hand> hands1 = new ArrayList<>(1000), hands2 = new ArrayList<>(1000);
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                hands1.add(Hand.parse(inputLine, true));
                hands2.add(Hand.parse(inputLine, false));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hands1.sort(null);
        for (int i = 0; i < hands1.size(); i++) {
            Hand hand = hands1.get(i);
            result1 += (long)hand.bid() * (i + 1);
        }
        System.out.println(hands1);
        hands2.sort(null);
        for (int i = 0; i < hands2.size(); i++) {
            Hand hand = hands2.get(i);
            result2 += (long)hand.bid() * (i + 1);
        }
        System.out.println(hands2);

        return new Utils.PuzzlePair(result1, result2);
    }

    public record Hand(HandType type, String hand, int bid, boolean firstHalf) implements Comparable<Hand> {
        public static Hand parse(String input, boolean firstHalf) {
            final String[] handAndBid = input.split(" ");
            if (handAndBid[0].length() != 5)
                throw new IllegalArgumentException(input);
            return new Hand(
                HandType.parse(handAndBid[0], firstHalf),
                handAndBid[0],
                Integer.parseInt(handAndBid[1]),
                firstHalf
            );
        }

        @Override
        public int compareTo(Hand other) {
            int ordinal = type.ordinal();
            int otherOrdinal = other.type.ordinal();
            if (ordinal > otherOrdinal)
                return 1;
            if (otherOrdinal > ordinal)
                return -1;
            // both must be of the same type
            char[] thisChars = hand.toCharArray();
            char[] otherChars = other.hand.toCharArray();
            for (int i = 0; i < 5; i++)
                if (isBetter(thisChars[i], otherChars[i]))
                    return 1;
                else if (isBetter(otherChars[i], thisChars[i]))
                    return -1;
            return 0;
        }

        private boolean isBetter(char better, char worse) {
            for (char c : firstHalf ? CARDS1 : CARDS2) {
                if (c == worse)
                    return false;
                if (c == better)
                    return true;
            }
            throw new IllegalArgumentException(better + "," + worse);
        }

        @Override
        public String toString() {
            return "Hand[" +
                   "type=" + type +
                   ", hand=" + hand +
                   ", bid=" + bid +
                   ']';
        }
    }

    public enum HandType {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_SAME, FULL_HOUSE, FOUR_SAME, ALL_SAME;
        public static HandType parse(String input, boolean firstHalf) {
            char[] chars = input.toCharArray();
            if (chars.length != 5)
                throw new IllegalArgumentException(input);
            Set<Character> set = new HashSet<>(5);
            for (char c : chars)
                set.add(c);
            int oldSize = set.size();
            if (!firstHalf && set.contains('J')) {
                set.remove('J');
                return switch (occurrences(chars, 'J')) { // must be greater than 0
                    case 1 -> {
                        byte mostOccurrences = 0;
                        for (Character c : set) {
                            final byte occurrences = occurrences(chars, c);
                            if (occurrences == 3)
                                yield FOUR_SAME;
                            if (occurrences == 4)
                                yield ALL_SAME;
                            if (occurrences > mostOccurrences)
                                mostOccurrences = occurrences;
                        }
                        yield switch (mostOccurrences) {
                            case 1 -> ONE_PAIR;
                            case 2 -> THREE_SAME;
                            default -> throw new IllegalStateException();
                        };
                    }
                    case 2 -> {
                        for (Character c : set) {
                            final byte occurrences = occurrences(chars, c);
                            if (occurrences == 3)
                                yield ALL_SAME;
                            if (occurrences == 2)
                                yield FOUR_SAME;
                        }
                        yield THREE_SAME;
                    }
                    case 3 -> {
                        final byte occurrences = occurrences(chars, set.iterator().next());
                        if (occurrences == 1)
                            yield FOUR_SAME;
                        yield ALL_SAME;
                    }
                    case 4, 5 -> ALL_SAME;
                    default -> throw new IllegalStateException();
                };
            }
            return switch (5 - oldSize) {
                case 0 -> HIGH_CARD;
                case 1 -> ONE_PAIR;
                case 2 -> {
                    for (Character c : set) {
                        final byte occurrences = occurrences(chars, c);
                        if (occurrences == 2)
                            yield TWO_PAIR;
                        if (occurrences == 3)
                            yield THREE_SAME;
                    }
                    throw new IllegalStateException();
                }
                case 3 -> {
                    for (Character c : set) {
                        final byte occurrences = occurrences(chars, c);
                        if (occurrences == 3)
                            yield FULL_HOUSE;
                        if (occurrences == 4)
                            yield FOUR_SAME;
                    }
                    throw new IllegalStateException();
                }
                case 4 -> ALL_SAME;
                default -> throw new IllegalArgumentException();
            };
        }

        private static byte occurrences(char[] chars, char key) {
            byte value = 0;
            for (char c : chars)
                if (c == key)
                    value++;
            return value;
        }
    }

    private Puzzle7() {
        throw new AssertionError("Cannot instantiate Puzzle6!");
    }
}
