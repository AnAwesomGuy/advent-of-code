package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Puzzle4 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/4/input");

    public static void main(final String... args) {
        Utils.PuzzleIntPair answer = solve();
        System.out.println("Answer for first half of puzzle 4: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 4: " + answer.secondHalf());
    }

    public static Utils.PuzzleIntPair solve() {
        int result1 = 0, result2 = 0;
        final Map<Integer, AtomicInteger> copies = new HashMap<>(200);
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                Card card = Card.parse(inputLine);
                result1 += card.getWinning();
                int frequency = copies.computeIfAbsent(card.cardNum(), AtomicInteger::new).get();
                result2 += frequency;
                for (int i = card.cardNum() + 1; i <= card.cardNum() + card.matchCount(); i++) {
                    copies.computeIfAbsent(i, integer -> new AtomicInteger()).getAndAdd(frequency);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Utils.PuzzleIntPair(result1, result2); // 5921508
    }

    public static final class Card {
        private static final Map<String, Card> CARD_NUMBERS = new HashMap<>(100);
        private static final Pattern CARD_PATTERN = Pattern.compile("Card +(\\d+):  ?([\\d ]+) \\|  ?([\\d ]+)");
        public static Card parse(final String input) {
            Matcher matcher = CARD_PATTERN.matcher(input);
            if (!matcher.matches())
                throw new IllegalArgumentException(input);
            String cardNum = matcher.group(1);
            Card card = new Card(
                Integer.parseInt(cardNum),
                Arrays.asList(matcher.group(2).split("  ?")),
                Arrays.asList(matcher.group(3).split("  ?"))
            );
            if (CARD_NUMBERS.putIfAbsent(cardNum, card) != null)
                throw new IllegalArgumentException("Card with this number " + card.cardNum + " has already been registered!");
            return card;
        }

        private final int cardNum;
        private final List<String> winning;
        private final List<String> numbers;
        private List<String> matches;

        private Card(int cardNum, List<String> winning, List<String> numbers) {
            this.cardNum = cardNum;
            this.winning = winning;
            this.numbers = numbers;
        }

        public int cardNum() {
            return cardNum;
        }

        public List<String> winning() {
            return winning;
        }

        public List<String> numbers() {
            return numbers;
        }

        public List<String> getMatches() {
            return List.copyOf(matches);
        }

        public int getWinning() {
            if (matches == null) {
                List<String> list = new ArrayList<>(numbers);
                if (list.retainAll(winning)) {
                    matches = list;
                    return 1 << list.size(); // 1 * 2 ^ matches.size()
                }
            } else return 1 << matches.size(); // 1 * 2 ^ matches.size()
            return 0;
        }

        public int matchCount() {
            if (matches == null) {
                List<String> list = new ArrayList<>(numbers);
                if (list.retainAll(winning))
                    matches = list;
                else return 0;
            }
            return matches.size();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Card card = (Card)o;
            return cardNum == card.cardNum && Objects.equals(winning, card.winning) && Objects.equals(numbers, card.numbers);
        }

        @Override
        public int hashCode() {
            return 31 * (31 * (cardNum ^ (cardNum >>> 16)) + winning.hashCode()) + numbers.hashCode();
        }
    }

    private Puzzle4() {
        throw new AssertionError("Cannot instantiate Puzzle4!");
    }
}
