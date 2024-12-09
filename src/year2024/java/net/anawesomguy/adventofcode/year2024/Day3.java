package net.anawesomguy.adventofcode.year2024;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AdventDay(day = 3)
public final class Day3 implements Puzzle.SingleLine {
    private static final Pattern MUL_REGEX = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    private String input = "";

    @Override
    public void input(String singeLine) throws InvalidInputException {
        this.input = singeLine;
    }

    @Override
    public int solvePart1() {
        return addMulInstructions(this.input);
    }

    @Override
    public int solvePart2() {
        return addMulInstructions(this.input.replaceAll("don't\\(\\)[\\s\\S]*?do\\(\\)", ""));
    }

    private static int addMulInstructions(String str) {
        int result = 0;
        Matcher matcher = MUL_REGEX.matcher(str);
        while (matcher.find())
            result += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        return result;
    }
}
