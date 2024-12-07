package net.anawesomguy.adventofcode.year2024;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle.LineBased;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public final class Day1 implements LineBased {
    private final IntList leftColumn = new IntArrayList(1000);
    private final IntList rightColumn = new IntArrayList(1000);

    @Override
    public void input(String line) {
        String[] numbers = StringUtils.split(line);
        if (numbers.length != 2)
            throw new InvalidInputException();
        leftColumn.add(Integer.parseInt(numbers[0]));
        rightColumn.add(Integer.parseInt(numbers[1]));
    }

    @Override
    public int solvePart1() {
        IntList column1 = this.leftColumn;
        IntList column2 = this.rightColumn;
        int size = column1.size();
        if (size != column2.size())
            throw new IllegalStateException();

        column1.unstableSort(null);
        column2.unstableSort(null);
        int result = 0;
        for (int i = 0; i < size; i++)
            result += Math.abs(column1.getInt(i) - column2.getInt(i));
        return result;
    }

    @Override
    public int solvePart2() {
//        @SuppressWarnings("deprecation")
//        Int2IntMap leftCountMap = new Int2IntOpenHashMap(
//            this.leftColumn.stream().collect(
//                Collectors.groupingBy(Function.identity(), Collectors.summingInt(o -> 1))));
//        @SuppressWarnings("deprecation")
//        Int2IntMap rightCountMap = new Int2IntOpenHashMap(
//            this.rightColumn.stream().collect(
//                Collectors.groupingBy(Function.identity(), Collectors.summingInt(o -> 1))));
//
//        leftCountMap.forEach((key, val) -> rightCountMap.mergeInt(key, val, (k, v) -> k * v));
//        return rightCountMap.int2IntEntrySet()
//                            .stream()
//                            .mapToInt((e) -> e.getIntKey() * e.getIntValue())
//                            .sum();

        // this is probably insanely inefficient but the weird mess above doesnt work so idk lol
        int result = 0;
        IntList rightColumn = this.rightColumn;
        for (int left : this.leftColumn)
            result += left * Collections.frequency(rightColumn, left);
        return result;
    }
}
