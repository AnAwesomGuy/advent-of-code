package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.stream.Stream;

@AdventDay(day = 4)
public final class Day4 implements Puzzle.LineStreamed {
    private boolean[][] grid;

    @Override
    public void input(Stream<String> stream) throws InvalidInputException {
        grid = stream.map(s -> {
            boolean[] c = new boolean[s.length()];
            for (int i = 0; i < c.length; i++)
                c[i] = s.charAt(i) == '@';
            return c;
        }).toArray(boolean[][]::new);
    }

    @Override
    public long solvePart1() {
        return accessible(this.grid, false);
    }

    @Override
    public long solvePart2() {
        long result = 0;
        int gLen = this.grid.length;
        boolean[][] grid = new boolean[gLen][];
        for (int i = 0; i < gLen; i++)
            grid[i] = this.grid[i].clone();
        long answer;
        while ((answer = accessible(grid, true)) > 0)
            result += answer;
        return result;
    }

    public static long accessible(boolean[][] grid, boolean modify) {
        long result = 0;
        for (int i = 0, gLen = grid.length; i < gLen; i++) {
            boolean[] row = grid[i];
            for (int j = 0, len = row.length; j < len; j++)
                if (row[j]) {
                    int around = 0;
                    if (i >= 1) {
                        boolean[] rowN1 = grid[i - 1];
                        if (j >= 1 && rowN1[j - 1])
                            around++;
                        if (rowN1[j])
                            around++;
                        if (j + 1 < len && rowN1[j + 1])
                            around++;
                    }
                    if (j >= 1 && row[j - 1])
                        around++;
                    if (j + 1 < len && row[j + 1])
                        around++;
                    if (i + 1 < gLen) {
                        boolean[] row1 = grid[i + 1];
                        if (j + 1 < len && row1[j + 1])
                            around++;
                        if (row1[j])
                            around++;
                        if (j >= 1 && row1[j - 1])
                            around++;
                    }
                    if (around < 4) {
                        if (modify)
                            row[j] = false;
                        result++;
                    }
                }
        }
        return result;
    }
}
