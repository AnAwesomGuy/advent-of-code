package net.anawesomguy.adventofcode.year2024;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle.LineStreamed;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Day4 implements LineStreamed {
    private XMASLetters[][] crossword; // 2d array of the input
    private final List<IntList> xIndices = new ArrayList<>();

    @Override
    public void input(Stream<String> stream) throws InvalidInputException {
        this.crossword = stream.map(s -> {
            int length = s.length();
            XMASLetters[] letters = new XMASLetters[length];
            IntList xIndices = new IntArrayList();
            for (int i = 0; i < length; i++)
                if ((letters[i] = XMASLetters.getFromChar(s.charAt(i))) == XMASLetters.X)
                    xIndices.add(i);
            Day4.this.xIndices.add(xIndices);
            return letters;
        }).toArray(XMASLetters[][]::new);
    }

    @Override
    public int solvePart1() {
        return 0;
    }

    @Override
    public int solvePart2() {
        return 0;
    }

    public enum XMASLetters {
        X, M, A, S;

        @Nullable
        public static XMASLetters getFromChar(char c) {
            return switch (c) {
                case 'X' -> X;
                case 'M' -> M;
                case 'A' -> A;
                case 'S' -> S;
                default -> null;
            };
        }
    }
}
