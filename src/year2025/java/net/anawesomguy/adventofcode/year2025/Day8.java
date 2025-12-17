package net.anawesomguy.adventofcode.year2025;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongLongImmutablePair;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@AdventDay(day = 8)
public final class Day8 implements Puzzle.LineStreamed {
    public static final Hash.Strategy<Object> IDENTITY_HASH = new Hash.Strategy<>() {
        @Override
        public int hashCode(Object o) {
            return System.identityHashCode(o);
        }

        @Override
        public boolean equals(Object a, Object b) {
            return a == b;
        }
    };

    private int junctionCount;
    private LongLongPair[] pairsByDistance;

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        long[] junctionBoxes = lines.mapToLong(Day8::posFromString).toArray();
        int len = this.junctionCount = junctionBoxes.length;
        List<LongLongPair> distances = new ArrayList<>(len * (len - 1) / 2);
        for (int i = 0; i < len; i++) {
            long junctionPos = junctionBoxes[i];
            for (int j = i + 1; j < len; j++) {
                long otherJunction = junctionBoxes[j];
                distances.add(new LongLongImmutablePair(junctionPos, otherJunction));
            }
        }
        pairsByDistance = distances.toArray(new LongLongPair[0]);
        Arrays.sort(pairsByDistance, Comparator.comparingLong(pair -> distSqr(pair.leftLong(), pair.rightLong())));
    }

    @Override
    public long solvePart1() {
        Long2ObjectMap<LongSet> circuits = new Long2ObjectOpenHashMap<>(1000, 0.99F);
        for (int i = 0, min = Math.min(pairsByDistance.length, 1000); i < min; i++) {
            LongLongPair pair = pairsByDistance[i];
            Supplier<@NotNull LongSet> newSet = Suppliers.memoize(() -> new LongOpenHashSet(2, 0.95F));
            var circuitLeft = circuits.get(pair.leftLong());
            if (circuitLeft == null) {
                circuitLeft = newSet.get();
                circuitLeft.add(pair.leftLong());
                circuits.put(pair.leftLong(), circuitLeft);
            }
            var circuitRight = circuits.computeIfAbsent(
                pair.rightLong(),
                k -> {
                    var set = newSet.get();
                    set.add(k);
                    return set;
                });
            if (circuitRight == circuitLeft)
                continue;
            if (circuitLeft.size() > circuitRight.size()) {
                circuitLeft.addAll(circuitRight);
                for (long p : circuitRight)
                    circuits.put(p, circuitLeft);
            } else {
                circuitRight.addAll(circuitLeft);
                for (long p : circuitLeft)
                    circuits.put(p, circuitRight);
            }
        }

        int first = 1, second = 1, third = 1;
        for (LongSet set : new ObjectOpenCustomHashSet<>(circuits.values(), 0.99F, IDENTITY_HASH)) {
            int size = set.size();
            if (size > first) {
                third = second;
                second = first;
                first = size;
            } else if (size > second) {
                third = second;
                second = size;
            } else if (size > third) {
                third = size;
            }
        }
        return (long)first * second * third;
    }

    @Override
    public long solvePart2() {
        Set<LongSet> uniqueCircuits = new ObjectOpenCustomHashSet<>(IDENTITY_HASH);
        Long2ObjectMap<LongSet> circuits = new Long2ObjectOpenHashMap<>(pairsByDistance.length / 2);
        for (LongLongPair pair : pairsByDistance) {
            Supplier<@NotNull LongSet> newSet = Suppliers.memoize(() -> {
                var set = new LongOpenHashSet(2, 0.95F);
                uniqueCircuits.add(set);
                return set;
            });
            var circuitLeft = circuits.get(pair.leftLong());
            if (circuitLeft == null) {
                circuitLeft = newSet.get();
                circuitLeft.add(pair.leftLong());
                circuits.put(pair.leftLong(), circuitLeft);
            }
            var circuitRight = circuits.computeIfAbsent(
                pair.rightLong(),
                k -> {
                    var set = newSet.get();
                    set.add(k);
                    return set;
                });
            if (circuitRight == circuitLeft)
                continue;
            if (circuitLeft.size() > circuitRight.size()) {
                circuitLeft.addAll(circuitRight);
                for (long p : circuitRight)
                    circuits.put(p, circuitLeft);
                uniqueCircuits.remove(circuitRight);
            } else {
                circuitRight.addAll(circuitLeft);
                for (long p : circuitLeft)
                    circuits.put(p, circuitRight);
                uniqueCircuits.remove(circuitLeft);
            }
            if (uniqueCircuits.size() == 1 && uniqueCircuits.iterator().next().size() == junctionCount)
                return getX(pair.leftLong()) * getX(pair.rightLong());
        }
        throw new InvalidInputException();
    }

    public static long posFromString(String s) {
        String[] split = s.split(",");
        return ((long)Integer.parseInt(split[0]) << 40) |
            ((long)Integer.parseInt(split[1]) << 20) |
            (long)Integer.parseInt(split[2]);
    }

    // 20 bit unsigned integers
    public static long distSqr(long pos1, long pos2) {
        long dx = getX(pos1) - getX(pos2);
        long dy = (pos1 >> 20 & 0xFFFFF) - (pos2 >> 20 & 0xFFFFF);
        long dz = (pos1 & 0xFFFFF) - (pos2 & 0xFFFFF);
        return dx * dx + dy * dy + dz * dz;
    }

    public static long getX(long pos) {
        return pos >> 40;
    }

    public static String posToString(long pos) {
        return getX(pos) + "," + (pos >> 20 & 0xFFFFF) + "," + (pos & 0xFFFFF);
    }
}
