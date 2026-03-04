package justmc;

import justmc.annotation.Inline;
import justmc.enums.NoiseRangeMode;

@Inline
public final class Math {
    private Math() {}

    public static final double E = 2.718281828459045;
    public static final double PI = 3.141592653589793;
    public static final double TAU = 2.0 * PI;
    public static final double DEGREES_TO_RADIANS = PI / 180.0;
    public static final double RADIANS_TO_DEGREES = 180.0 / PI;

    public static double pow(double x, double power) {
        var result = Variable.temp();
        Unsafe.operation("set_variable_power", CopyableMap.of(
                Pair.of("number", NumberPrimitive.of(x)),
                Pair.of("power", NumberPrimitive.of(power))
        ));
        return Unsafe.asDouble(result);
    }

    public static native double square(double x);

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
