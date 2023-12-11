package net.anawesomguy.adventofcode.puzzles;

import net.anawesomguy.adventofcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;

public final class Puzzle5 {
    public static final URI INPUT_URL = Utils.newURI("https://adventofcode.com/2023/day/5/input");

    public static void main(final String... args) {
        Utils.PuzzlePair answer = solve();
        System.out.println("Answer for first half of puzzle 5: " + answer.firstHalf());
        System.out.println("Answer for second half of puzzle 5: " + answer.secondHalf());
    }

    public static Utils.PuzzlePair solve() {
        final Map<MapType, List<CropMapTriple>> map = new EnumMap<>(MapType.class);
        final LongStream seeds1, seeds2;
        try (final BufferedReader br = Utils.getReader(INPUT_URL)) {
            String[] seeds = br.readLine().substring(7).split(" ");
            seeds1 = Arrays.stream(seeds).mapToLong(Long::parseLong);
            seeds2 = Arrays.stream(SeedRange.parse(seeds)).mapMultiToLong((seedRange, consumer) -> {
                for (long i = seedRange.start(); i < seedRange.start() + seedRange.length(); i++)
                    consumer.accept(i);
            });

            String inputLine;
            List<CropMapTriple> currentList = null;
            while ((inputLine = br.readLine()) != null) {
                if (inputLine.isBlank())
                    continue;
                if (Character.isDigit(inputLine.charAt(0))) //noinspection DataFlowIssue (should never happen)
                    currentList.add(CropMapTriple.parse(inputLine));
                else
                    map.put(MapType.from(inputLine), currentList = new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LongUnaryOperator seedToLocation = seed -> {
            long currentValue = seed;
            for (MapType type : MapType.values())
                for (CropMapTriple cropMap : map.get(type)) {
                    final long destination = cropMap.getDestinationFor(currentValue);
                    if (destination != currentValue) {
                        currentValue = destination;
                        break;
                    }
                }
            return currentValue;
        };
        return new Utils.PuzzlePair(
            getMin(seeds1, seedToLocation),
            getMin(seeds2, seedToLocation)
        );
    }

    private static long getMin(LongStream stream, LongUnaryOperator mapper) {
        return stream.map(mapper).min().orElse(0);
    }

    public record CropMapTriple(long destination, long source, long length) {
        public static CropMapTriple parse(final String input) {
            final String[] ints = input.split(" ");
            return new CropMapTriple(
                Long.parseLong(ints[0]),
                Long.parseLong(ints[1]),
                Long.parseLong(ints[2])
            );
        }

        public long getDestinationFor(long input) {
            if (input >= source + length || input < source)
                return input;
            return input + destination - source;
        }
    }

    public record SeedRange(long start, long length) {
        public static SeedRange[] parse(final String[] seeds) {
            if (seeds.length % 2 != 0 || seeds.length < 1)
                throw new IllegalArgumentException(Arrays.toString(seeds));
            SeedRange[] result = new SeedRange[seeds.length / 2];
            for (int i = 0; i < seeds.length; i += 2)
                result[i / 2] = new SeedRange(Long.parseLong(seeds[i]), Long.parseLong(seeds[i + 1]));
            return result;
        }
    }

    public enum MapType {
        SEED_TO_SOIL("seed-to-soil"),
        SOIL_TO_FERTILIZER("soil-to-fertilizer"),
        FERTILIZER_TO_WATER("fertilizer-to-water"),
        WATER_TO_LIGHT("water-to-light"),
        LIGHT_TO_TEMPERATURE("light-to-temperature"),
        TEMPERATURE_TO_HUMIDITY("temperature-to-humidity"),
        HUMIDITY_TO_LOCATION("humidity-to-location");

        public static MapType from(String name) {
            for (MapType type : values())
                if (name.toLowerCase().startsWith(type.name))
                    return type;
            return null;
        }

        private final String name;

        MapType(String name) {
            this.name = name;
        }
    }

    private Puzzle5() {
        throw new AssertionError("Cannot instantiate Puzzle5!");
    }
}
