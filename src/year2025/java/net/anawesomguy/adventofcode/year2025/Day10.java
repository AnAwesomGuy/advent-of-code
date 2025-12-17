package net.anawesomguy.adventofcode.year2025;

import net.anawesomguy.adventofcode.AdventDay;
import net.anawesomguy.adventofcode.InvalidInputException;
import net.anawesomguy.adventofcode.Puzzle;

import java.util.Arrays;
import java.util.stream.Stream;

@AdventDay(day = 10)
public final class Day10 implements Puzzle.LineStreamed {
    private Machine[] machines;

    @Override
    public void input(Stream<String> lines) throws InvalidInputException {
        machines = lines.map(Machine::fromString).toArray(Machine[]::new);
    }

    @Override
    public long solvePart1() {
        long result = 0;
        for (var machine : machines) {
            long fewest = Machine.fewestPresses(machine.lights(), machine.buttons());
            result += fewest;
        }
        return result;
    }

    @Override
    public long solvePart2() {
        return Arrays.stream(machines)
                     .parallel()
                     .mapToLong(Machine::fewestPresses)
                     .peek(System.out::println)
                     .sum();
    }

    // lights is a "bit set" from left to right, same with buttons
    // this allows us to xor a button with the lights to get the result easily
    // 0x8000 is a 16-bit integer with the top bit set
    public record Machine(char lights, char[] buttons, short[] joltages) {
        public static char lightsFrom(String s) {
            int i = s.length() - 1;
            char lights = 0;
            while (i-- > 1) {
                lights >>>= 1;
                if (s.charAt(i) == '#')
                    lights |= 0x8000;
            }
            return lights;
        }

        public static char buttonsFrom(String s) {
            String[] split = s.substring(1, s.length() - 1).split(",");
            char buttons = 0;
            for (String string : split)
                buttons |= (char)(0x8000 >>> Integer.parseUnsignedInt(string));
            return buttons;
        }

        public static short[] joltagesFrom(String s) {
            String[] split = s.substring(1, s.length() - 1).split(",");
            int n = split.length;
            short[] i = new short[n];
            while (n-- > 0)
                i[n] = (short)Integer.parseInt(split[n]);
            return i;
        }

        public static Machine fromString(String s) {
            String[] split = s.split(" ");
            int n = split.length - 2;
            char[] buttons = new char[n];
            while (n-- > 0)
                buttons[n] = buttonsFrom(split[n + 1]);
            return new Machine(lightsFrom(split[0]), buttons, joltagesFrom(split[split.length - 1]));
        }

        public static long fewestPresses(char lights, char[] buttons) {
            long depth = 0;
            while (true) {
                long press = presses(lights, buttons, 0, depth += 3);
                if (press != Long.MIN_VALUE)
                    return depth - press + 1;
            }
        }

        private static long presses(char lights, char[] buttons, int start, long depth) {
            long largest = Long.MIN_VALUE;
            for (int i = start, len = buttons.length; i < len; i++) {
                char button = buttons[i];
                if (lights == button)
                    return depth;
                if (depth > 1 && i + 1 < len) {
                    long press = presses((char)(lights ^ button), buttons, i + 1, depth - 1);
                    if (press > largest)
                        largest = press;
                }
            }
            return largest;
        }

        public long fewestPresses() {
            var shorts = new short[joltages.length];
            long depth = 4;
            while (true) {
                long press = presses(lights, buttons, depth += 6, joltages, shorts);
                if (press != Long.MIN_VALUE)
                    return depth - press + 1;
            }
        }

        private static long presses(char lights, char[] buttons, long depth, short[] joltagesTarget, short[] joltages) {
            long largest = Long.MIN_VALUE;
            long newDepth = depth;
            outer:
            for (char button : buttons) {
                boolean valid = true;
                short[] joltagesCopy = Arrays.copyOf(joltages, joltagesTarget.length);
                {
                    int i = 0;
                    char buttonC = button;
                    do {
                        if ((buttonC >>> 15 & 1) == 0) {
                            i++;
                            continue;
                        }
                        short targetJoltage = joltagesTarget[i];
                        short joltage = ++joltagesCopy[i++]; // 0 or 1
                        if (joltage > targetJoltage)
                            continue outer;
                        valid &= joltage == targetJoltage;
                    } while ((buttonC <<= 1) != 0);
                }
                if (valid)
                    return depth;
                if (newDepth > 1) {
                    long press = presses((char)(lights ^ button), buttons, newDepth - 1, joltagesTarget, joltagesCopy);
                    if (press > largest && (newDepth = depth - (largest = press)) == 1)
                        return press;
                }
            }
            return largest;
        }
    }
}
