package net.anawesomguy.adventofcode.year2024;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle.LineBased;

public final class Day1 implements LineBased {
    private final IntList column1 = new IntArrayList(1000);
    private final IntList column2 = new IntArrayList(1000);

    @Override
    public void input(String line) {
        String[] numbers = line.split(" {3}");
        if (numbers.length != 2)
            throw new InvalidInputException();
        column1.add(Integer.parseInt(numbers[0]));
        column2.add(Integer.parseInt(numbers[1]));
    }

    public void init() {
        this.column1.sort(null);
        this.column2.sort(null);
    }

    @Override
    public int solvePart1() {
        int[] column1 = this.column1.toIntArray();
        int[] column2 = this.column2.toIntArray();
        if (column1.length != column2.length)
            throw new IllegalStateException();

        int result = 0;
        for (int i = 0; i < column1.length; i++)
            result += Math.abs(column1[i] - column2[i]);
        return result;
    }

    @Override
    public int solvePart2() {
        return 0;
    }
}
