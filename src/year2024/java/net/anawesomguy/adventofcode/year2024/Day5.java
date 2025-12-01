package net.anawesomguy.adventofcode.year2024;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle.LineStreamed;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Not finished (i think i forgot when i did this)
 */
public final class Day5 implements LineStreamed {
    private List<IntIntPair> section1;
    private List<int[]> section2;

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        Pair<List<IntIntPair>, List<int[]>> pair =
            lines.sequential()
                 .collect(() -> new ImmutablePair<>(new ArrayList<>(), new ArrayList<>()),
                          new BiConsumer<>() {
                              private boolean section2;

                              @Override
                              public void accept(ImmutablePair<List<IntIntPair>, List<int[]>> pair, String str) {
                                  if (str.isBlank()) {
                                      section2 = true;
                                      return;
                                  }
                                  if (!section2) {
                                      String[] split = str.split("\\|");
                                      if (split.length != 2)
                                          throw new InvalidInputException(str);
                                      pair.getLeft().add(
                                          new IntIntImmutablePair(Integer.parseInt(split[0].trim()),
                                                                  Integer.parseInt(split[1].trim())));
                                  } else {
                                      String[] split = str.split(",");
                                      int length = split.length;
                                      int[] ints = new int[length];
                                      for (int i = 0; i < length; i++)
                                          ints[i] = Integer.parseInt(split[i].trim());
                                      pair.getRight().add(ints);
                                  }
                              }
                          },
                          (l, r) -> {
                              l.left.addAll(r.left);
                              l.right.addAll(r.right);
                          }
                 );
        section1 = pair.getLeft();
        section2 = pair.getRight();
    }

    @Override
    public long solvePart1() {
        return 0;
    }

    @Override
    public long solvePart2() {
        return 0;
    }
}
