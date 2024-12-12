package net.anawesomguy.adventofcode.year2024;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle.LineStreamed;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Day4 implements LineStreamed {
    private XMASLetter[][] crossword; // 2d array of the input
    private final List<IntList> xIndices = new ArrayList<>();

    @Override
    public void input(Stream<String> stream) throws InvalidInputException {
        if ((this.crossword = stream.map(s -> {
                int length = s.length();
                XMASLetter[] letters = new XMASLetter[length];
                IntList xIndices = new IntArrayList();
                for (int i = 0; i < length; i++)
                    if ((letters[i] = XMASLetter.getFromChar(s.charAt(i))) == XMASLetter.X)
                        xIndices.add(i);
                Day4.this.xIndices.add(xIndices);
                return letters;
            }).toArray(XMASLetter[][]::new)).length != xIndices.size())
            throw new InvalidInputException();
    }

    @Override
    public int solvePart1() {
        int result = 0;
        XMASLetter[][] crossword = this.crossword;
        List<IntList> xIndices = this.xIndices;
        int size = xIndices.size();
        for (int y = 0; y < size; y++) {
            IntList ints = xIndices.get(y);
            XMASLetter[] letters = crossword[y];
            for (int index : ints) {
                XMASLetter letter = letters[index];
                if (letter == null)
                    continue;
                assert letter == XMASLetter.X;
                for (Direction dir : Direction.VALUES) {
                    XMASLetter current = letter;
                    MutableInt cursor = new MutableInt(1);
                    while ((current = current.next()) != null &&
                           current == dir.getFromOffset(crossword, index, y, cursor)) {
                        if (current == XMASLetter.S) {
                            result++;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int solvePart2() {
        return 0;
    }

    public enum XMASLetter {
        X, M, A, S;

        private static final XMASLetter[] VALUES = values();

        @Nullable
        public static Day4.XMASLetter getFromChar(char c) {
            return switch (c) {
                case 'X' -> X;
                case 'M' -> M;
                case 'A' -> A;
                case 'S' -> S;
                default -> null;
            };
        }

        public XMASLetter next() {
            int ordinal = this.ordinal();
            return ordinal >= VALUES.length ? null : VALUES[ordinal + 1];
        }
    }

    public enum Direction {
        UP_RIGHT(1, 1),
        UP(0, 1),
        UP_LEFT(-1, 1),
        RIGHT(1, 0),
        LEFT(-1, 0),
        DOWN_RIGHT(1, -1),
        DOWN(0, -1),
        DOWN_LEFT(-1, -1);

        static final Direction[] VALUES = values();

        public final int xOffset;
        public final int yOffset;

        Direction(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public <T> T getFromOffset(T[][] arr2d, int x, int y, MutableInt cursor) {
            int cur = cursor.getAndIncrement();
            int newY = y - (yOffset * cur);
            if (newY < 0 || newY >= arr2d.length)
                return null;
            T[] arr = arr2d[newY];
            int newX = x + (xOffset * cur);
            if (newX < 0 || newX >= arr.length)
                return null;
            return arr[newX];
        }
    }
}
