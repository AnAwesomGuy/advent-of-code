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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Puzzle8 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/8/input");
    public static void main(final String... args) {
        Utils.PuzzlePair answer = solve();
        System.out.println("Answer for first half of puzzle 2: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 2: " + answer.secondHalf());
    }

    public static Utils.PuzzlePair solve() {
        final String directions;
        final Map<String, Node> nodes = new HashMap<>();
        List<Node> nodes2 = new ArrayList<>();
        long result1 = 0, result2; // result1 = first half, result2 = second half
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            directions = br.readLine();
            br.readLine(); // skip next line (its empty)
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                final Node node = Node.parse(inputLine);
                nodes.put(node.id, node);
                if (node.id.endsWith("A"))
                    nodes2.add(node);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Node currentNode = nodes.get("AAA");
        long[] lcm = new long[nodes2.size()];
        int[] counter = new int[]{0, -1}; // bypass lambda restrictions
        boolean firstHalf = true, secondHalf = true;
        for (int i = 0;; i++) {
            if (i == directions.length())
                i = 0;
            char direction = directions.charAt(i);
            if (firstHalf) {
                final String nextNode;
                if (direction == 'L')
                    nextNode = currentNode.left();
                else if (direction == 'R')
                    nextNode = currentNode.right();
                else
                    throw new IllegalStateException();
                if ("ZZZ".equals(nextNode))
                    firstHalf = false;
                currentNode = nodes.get(nextNode);
                result1++;
            }
            if (secondHalf) {
                final boolean isLeft;
                if (direction == 'L')
                    isLeft = true;
                else if (direction == 'R')
                    isLeft = false;
                else
                    throw new IllegalStateException();

                final List<Node> toRemove = new ArrayList<>(1);
                nodes2.replaceAll(node -> {
                    final Node nextNode = nodes.get(isLeft ? node.left() : node.right());
                    if (nextNode.id().endsWith("Z")) {
                        toRemove.add(nextNode);
                        lcm[counter[0]++] = counter[1];
                    }
                    return nextNode;
                });
                nodes2.removeAll(toRemove);
                System.out.println(Arrays.toString(lcm));
                if (nodes2.isEmpty())
                    secondHalf = false;
                counter[1]++;
            }
            if (!firstHalf && !secondHalf)
                break;
        }
        result2 = Utils.lcm(lcm);
        return new Utils.PuzzlePair(result1, result2);
    }

    public record Node(String id, String left, String right) {
        private static final Pattern NODE_PATTERN = Pattern.compile("([1-9A-Z]{3}) = \\(([1-9A-Z]{3}), ([1-9A-Z]{3})\\)");

        public static Node parse(String input) {
            Matcher matcher = NODE_PATTERN.matcher(input);
            if (!matcher.matches())
                throw new IllegalStateException();
            return new Node(matcher.group(1), matcher.group(2), matcher.group(3));
        }
    }

    private Puzzle8() {
        throw new AssertionError("Cannot instantiate Puzzle8!");
    }
}
