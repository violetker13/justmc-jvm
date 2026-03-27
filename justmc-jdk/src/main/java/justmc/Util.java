package justmc;

import justmc.annotation.Inline;

import java.util.function.Consumer;

@Inline
public final class Util {
    public static long measureNanoTime(Runnable block) {
        var result = Variable.result();
        Unsafe.operation("controller_measure_time", MapPrimitive.of(
                Pair.of("variable", result)
        ), block);
        return Unsafe.asLong(result);
    }

    public static long measureTimeMicros(Runnable block) {
        var result = Variable.result();
        Unsafe.operation("controller_measure_time", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("duration", EnumPrimitive.of("MICROSECONDS"))
        ), block);
        return Unsafe.asLong(result);
    }

    public static long measureTimeMillis(Runnable block) {
        var result = Variable.result();
        Unsafe.operation("controller_measure_time", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("duration", EnumPrimitive.of("MILLISECONDS"))
        ), block);
        return Unsafe.asLong(result);
    }

    public static void repeatOnGrid(Location start, Location end, Consumer<Location> block) {
        var location = Variable.temp();
        Unsafe.operation("repeat_on_grid", MapPrimitive.of(
                Pair.of("variable", location),
                Pair.of("start", start),
                Pair.of("end", end)
        ), () -> {
            block.accept(Unsafe.cast(location));
        });
    }
}
