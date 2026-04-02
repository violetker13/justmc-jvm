package justmc;

import justmc.annotation.Inline;
import justmc.enums.TimeUnit;

@Inline
public final class Thread {
    private Thread() {}

    public static void wait(int ticks) {
        Unsafe.operation("control_wait", MapPrimitive.of(
                Pair.of("duration", NumberPrimitive.of(ticks))
        ));
    }

    public static void wait(int duration, TimeUnit timeUnit) {
        Unsafe.operation("control_wait", MapPrimitive.of(
                Pair.of("duration", NumberPrimitive.of(duration)),
                Pair.of("time_unit", timeUnit)
        ));
    }

    public static void awaitCpu() {
        while (World.getCpu() >= 60) {
            wait(1);
        }
    }

    public static void kill() {
        Unsafe.operation("control_end_thread", MapPrimitive.empty());
    }

    public static void stopRepeat() {
        Unsafe.operation("control_stop_repeat", MapPrimitive.empty());
    }

    public static void skipIteration() {
        Unsafe.operation("control_skip_iteration", MapPrimitive.empty());
    }

    public static void returnFunction() {
        Unsafe.operation("control_return_function", MapPrimitive.empty());
    }

    public static void warn(Text message) {
        Unsafe.operation("control_call_exception", MapPrimitive.of(
                Pair.of("message", message),
                Pair.of("type", EnumPrimitive.of("WARNING"))
        ));
    }

    public static void error(Text message) {
        Unsafe.operation("control_call_exception", MapPrimitive.of(
                Pair.of("message", message),
                Pair.of("type", EnumPrimitive.of("ERROR"))
        ));
    }

    public static void fatalError(Text message) {
        Unsafe.operation("control_call_exception", MapPrimitive.of(
                Pair.of("message", message),
                Pair.of("type", EnumPrimitive.of("FATAL"))
        ));
    }
}
