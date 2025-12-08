package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.ArrayList;
import java.util.List;

@AdventDay(day = 7)
public final class Day7 implements Puzzle.SingleLine {
    private int startingCol;
    private boolean[][] splittersMap;

    @Override
    public void input(String singleLine) throws InvalidInputException {
        String[] lines = singleLine.split("\n");
        startingCol = lines[0].indexOf('S');
        List<boolean[]> list = new ArrayList<>(lines.length / 2);
        for (int i = 2; i < lines.length; i += 2) {
            String s = lines[i];
            int len = s.length();
            boolean[] b = new boolean[len];
            while (len --> 0)
                b[len] = s.charAt(len) == '^';
            list.add(b);
        }
        splittersMap = list.toArray(new boolean[0][]);
    }

    @Override
    public long solvePart1() {
        // store the beams into an array with the position of S initially set
        // iterate over each row and iterate over each position in the row
        // if that position has a splitter and also a beam then it removes the beam and sets it to the left and right
        // each time a splitter is encountered where a beam also is, the result is incremented
        long result = 0L;
        boolean[] beams = new boolean[splittersMap[0].length]; // row of all the beams
        beams[startingCol] = true; // beam at the starting column
        for (boolean[] splitters : splittersMap) // iterate each row
            for (int i = splitters.length - 2; i > 0; i--) // each position in the row
                if (splitters[i] && beams[i]) { // if a beam was previously here and there is a splitter here
                    // remove the beam underneath and set it to the left and right
                    beams[i] = false;
                    beams[i + 1] = true;
                    beams[i - 1] = true;
                    result++;
                }
        return result;
    }

    @Override
    public long solvePart2() {
        long[] beams = new long[splittersMap[0].length];
        beams[startingCol] = 1;
        for (boolean[] splitters : splittersMap)
            for (int i = splitters.length - 2; i > 0; i--) // skip the very left and right positions
                if (splitters[i] && beams[i] != 0) {
                    beams[i + 1] += beams[i];
                    beams[i - 1] += beams[i];
                    beams[i] = 0;
                }
        long sum = 0L;
        for (long beam : beams)
            sum += beam;
        return sum;
    }
}
