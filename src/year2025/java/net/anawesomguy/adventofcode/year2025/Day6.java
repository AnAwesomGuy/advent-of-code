package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

@AdventDay(day = 6)
public final class Day6 implements Puzzle.SingleLine {
    private String[][] numbers;
    private boolean[] operations; // true for *, false for +

    @Override
    public void input(String singleLine) throws InvalidInputException {
        String[] lines = singleLine.split("\n");
        int lineLength = lines[0].length();
        int numLines = lines.length - 1;

        String[] operations = lines[numLines].split(" +");
        int length = operations.length;
        this.operations = new boolean[length];
        for (int i = 0; i < length; i++)
            this.operations[i] = operations[i].charAt(0) == '*';

        numbers = new String[numLines][length];
        int prevIndex = 0;
        for (int i = 0, j = 0; i < lineLength; i++) {
            boolean allEmpty = true;
            for (int k = 0; k < numLines; k++)
                if (lines[k].charAt(i) != ' ') {
                    allEmpty = false;
                    break;
                }
            if (allEmpty) {
                for (int k = 0; k < numLines; k++)
                    numbers[k][j] = lines[k].substring(prevIndex, i);
                prevIndex = i + 1;
                j++;
            }
        }
        for (int k = 0; k < numLines; k++)
            numbers[k][length - 1] = lines[k].substring(prevIndex, lineLength);
    }

    @Override
    public long solvePart1() {
        long result = 0;
        for (int i = 0; i < operations.length; i++) {
            if (operations[i]) { // multiply
                long j = 1;
                for (String[] ss : numbers)
                    j *= Integer.parseInt(ss[i].trim());
                result += j;
            } else { // add
                for (String[] ss : numbers)
                    result += Integer.parseInt(ss[i].trim());
            }
        }
        return result;
    }

    @Override
    public long solvePart2() {
        // i'm supposed to read it right to left but + and * and commutative
        long result = 0;
        for (int i = 0; i < operations.length; i++) { // iterate over each column of numbers
            int length = numbers[0][i].length(); // amount of numbers in the column
            if (operations[i]) { // multiply
                long mulResult = 1; // result of multiplication
                for (int j = 0; j < length; j++) // iterate over each char
                    mulResult *= numberInColumn(i, j);
                result += mulResult;
            } else { // add
                for (int j = 0; j < length; j++) // iterate over each char
                    result += numberInColumn(i, j);
            }
        }
        return result;
    }
    
    private int numberInColumn(int group, int column) {
        int num = 0;
        for (String[] number : numbers) { // iterate over each row
            char c = number[group].charAt(column); // the columnth char in group
            if (c == ' ')
                continue;
            num *= 10; // push back one digit
            num += c - '0'; // add this digit
        }
        return num;
    }
}
