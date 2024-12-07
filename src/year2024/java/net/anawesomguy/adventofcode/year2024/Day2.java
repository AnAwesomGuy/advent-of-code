package net.anawesomguy.adventofcode.year2024;

import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle.LineBased;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public final class Day2 implements LineBased {
    private final List<int[]> reports = new ArrayList<>();
    private static final int MAX_JUMP = 3, MIN_JUMP = 1;

    @Override
    public void input(String line) throws InvalidInputException {
        String[] split = line.split(" ");
        int length = split.length;
        if (length < 2)
            throw new InvalidInputException();
        int[] levels = new int[length];
        for (int i = 0; i < length; i++)
            levels[i] = Integer.parseInt(split[i]);
        reports.add(levels);
    }

    @Override
    public int solvePart1() {
        int result = 0;
        for (int[] report : this.reports)
            if (isReportSafe(report))
                result++;
        return result;
    }

    @Override
    public int solvePart2() {
        int result = 0;
        for (int[] report : this.reports)
            for (int i = 0, length = report.length; i < length; i++)
                // pretty inefficient but i honestly dont have a better way
                if (isReportSafe(ArrayUtils.remove(report, i))) {
                    result++;
                    break;
                }
        return result;
    }

    public static boolean isReportSafe(int[] report) {
        boolean ascending = false;
        for (int i = 0, length = report.length, prev = -1; i < length; i++) {
            int level = report[i];
            if (i == 0) { // first iteration (sets previous and continues)
                prev = level;
                continue;
            }

            if (prev == level)
                return false;

            if (i == 1) // second iteration
                ascending = prev < level;
            else if (!ascending) {
                if (prev < level) // ascending but (!ascending)
                    return false;
            } else if (prev > level) // descending but (ascending)
                return false;

            int difference = Math.abs(level - prev);
            // no need to check lower cuz absolute value is always positive
            // and a difference of 0 was already checked for
            if (difference > MAX_JUMP)
                return false;

            prev = level;
        }

        return true;
    }
}
