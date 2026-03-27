package justmc;

import justmc.annotation.Inline;

@Inline
public final class Location extends Primitive {
    private Location() {}

    public static Location of(double x, double y, double z, float yaw, float pitch) {
        var result = Variable.result();
        Unsafe.operation("set_variable_set_all_coordinates", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("x", NumberPrimitive.of(x)),
                Pair.of("y", NumberPrimitive.of(y)),
                Pair.of("z", NumberPrimitive.of(z)),
                Pair.of("yaw", NumberPrimitive.of(yaw)),
                Pair.of("pitch", NumberPrimitive.of(pitch))
        ));
        return Unsafe.cast(result);
    }

    public static Location of(double x, double y, double z) {
        return of(x, y, z, 0f, 0f);
    }

    public Location setX(double x) {
        var result = Variable.result();
        Unsafe.operation("set_variable_set_coordinate", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("coordinate", NumberPrimitive.of(x)),
                Pair.of("type", EnumPrimitive.of("X"))
        ));
        return Unsafe.cast(result);
    }

    public double getX() {
        var result = Variable.result();
        Unsafe.operation("set_variable_get_coordinate", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", EnumPrimitive.of("X"))
        ));
        return Unsafe.cast(result);
    }

    public double getY() {
        var result = Variable.result();
        Unsafe.operation("set_variable_get_coordinate", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", EnumPrimitive.of("Y"))
        ));
        return Unsafe.asDouble(result);
    }

    public double getZ() {
        var result = Variable.result();
        Unsafe.operation("set_variable_get_coordinate", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", EnumPrimitive.of("Z"))
        ));
        return Unsafe.asDouble(result);
    }

    public double getYaw() {
        var result = Variable.result();
        Unsafe.operation("set_variable_get_coordinate", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", EnumPrimitive.of("YAW"))
        ));
        return Unsafe.asDouble(result);
    }

    public double getPitch() {
        var result = Variable.result();
        Unsafe.operation("set_variable_get_coordinate", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", EnumPrimitive.of("PITCH"))
        ));
        return Unsafe.asDouble(result);
    }
}
