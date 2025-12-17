package net.anawesomguy.adventofcode.year2025;

import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.function.IntToLongFunction;
import java.util.stream.Stream;

@AdventDay(day = 11)
public final class Day11 implements Puzzle.LineStreamed {
    private static final int YOU = toInt("you"), OUT = toInt("out"),
        SVR = toInt("svr"), DAC = toInt("dac"), FFT = toInt("fft");

    // int is in the form: 0x00XXYYZZ where XX, YY, and ZZ are the bytes of the first, second, and third chars respectively
    private Int2ObjectMap<IntList> rack;

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        rack = lines.collect(
            Int2ObjectOpenHashMap::new,
            (map, s) -> {
                String[] split = s.split(" ");
                var list = map.computeIfAbsent(toInt(split[0]), k -> new IntArrayList(split.length - 1));
                for (int i = 1; i < split.length; i++)
                    list.add(toInt(split[i]));
            }, Int2ObjectMap::putAll);
    }

    private static int toInt(String s) {
        return ((byte)s.charAt(0) << 16) | ((byte)s.charAt(1) << 8) | (byte)s.charAt(2);
    }

    private static String toString(int i) {
        // casting to byte automatically removes the extra upper bits
        return new String(new byte[]{(byte)(i >> 16), (byte)(i >> 8), (byte)i});
    }

    @Override
    public long solvePart1() {
        return traverse(YOU, OUT, new IntOpenHashSet(), new Int2LongOpenHashMap(rack.size()));
    }

    @Override
    public long solvePart2() {
        var cache = new Int2LongOpenHashMap(rack.size());
        var set = new IntOpenHashSet();
        long dacToFft = traverse(DAC, FFT, set, cache);
        cache.clear();
        if (dacToFft != 0) { // if there is a path from dac -> fft, there can't be a path from fft -> dac, or it would loop
            long svrToDac = traverse(SVR, DAC, set, cache);
            cache.clear();
            return svrToDac * dacToFft * traverse(FFT, OUT, set, cache);
        } else {
            long fftToDac = traverse(FFT, DAC, set, cache);
            cache.clear();
            long svrToFft = traverse(SVR, FFT, set, cache);
            cache.clear();
            return svrToFft * fftToDac * traverse(DAC, OUT, set, cache);
        }
    }

    private long traverse(int current, int end, IntSet visited, Int2LongMap cache) {
        return current == end ? 1 :
            ((current == OUT || visited.contains(current)) ? 0 :
                cache.computeIfAbsent(current, (IntToLongFunction)i -> { // current == i
                    visited.add(i);
                    var list = rack.get(i);
                    long total = 0L;
                    for (int j = 0, size = list.size(); j < size; j++)
                        total += traverse(list.getInt(j), end, visited, cache);
                    visited.remove(i);
                    return total;
                }));
    }
}
