package justmc;

import justmc.annotation.Inline;
import justmc.enums.NoiseRangeMode;

@Inline
public final class Math {
    public static final double E = 2.718281828459045;
    public static final double PI = 3.141592653589793;
    public static final double TAU = 2.0 * PI;
    public static final double DEGREES_TO_RADIANS = 0.017453292519943295;
    public static final double RADIANS_TO_DEGREES = 57.29577951308232;

    private Math() {}

    public static int min(int... numbers) {
        var result = Variable.result();
        Unsafe.operation("set_variable_min", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("value", ListPrimitive.of(numbers))
        ));
        return Unsafe.asInt(result);
    }

    public static double min(double... numbers) {
        var result = Variable.result();
        Unsafe.operation("set_variable_min", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("value", ListPrimitive.of(numbers))
        ));
        return Unsafe.asDouble(result);
    }

    public static double pow(double x, double power) {
        var result = Variable.result();
        Unsafe.operation("set_variable_power", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("number", NumberPrimitive.of(x)),
                Pair.of("power", NumberPrimitive.of(power))
        ));
        return Unsafe.asDouble(result);
    }

    public static double square(double x) {
        var result = Variable.result();
        Unsafe.operation("set_variable_power", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("number", NumberPrimitive.of(x))
        ));
        return Unsafe.asDouble(result);
    }

    public static native double root(double x, double power);
    public static native double sqrt(double x);
    public static native double log(double x, double base);

    public static native double random();
    public static native double random(double from, double to);
    public static native int randint(int from, int to);

    public static native double sindeg(double a);
    public static native double sin(double a);
    public static native double asin(double a);
    public static native double sinh(double a);
    public static native double asinh(double a);

    public static native double perlinNoise(
            Location location,
            long seed,
            double locationFrequency,
            double octaves,
            double frequency,
            double amplitude,
            NoiseRangeMode rangeMode,
            boolean normalized
    );
}
