package justmc;

import justmc.annotation.Inline;
import justmc.enums.LocationCoordinate;

@Inline
public final class Location implements Primitive {
    private Location() {}

    public static native Location of(double x, double y, double z, float yaw, float pitch);

    public static Location of(double x, double y, double z) {
        return of(x, y, z, 0f, 0f);
    }

    public Location setX(double x) {
        var result = Variable.temp();
        Unsafe.operation("set_variable_set_coordinate", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("coordinate", NumberPrimitive.of(x)),
                Pair.of("type", LocationCoordinate.X)
        ));
        return Unsafe.asLocation(result);
    }

    public double getX() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_get_coordinate", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", LocationCoordinate.X)
        ));
        return Unsafe.asDouble(result);
    }

    public double getY() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_get_coordinate", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", LocationCoordinate.Y)
        ));
        return Unsafe.asDouble(result);
    }

    public double getZ() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_get_coordinate", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", LocationCoordinate.Z)
        ));
        return Unsafe.asDouble(result);
    }

    public double getYaw() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_get_coordinate", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", LocationCoordinate.YAW)
        ));
        return Unsafe.asDouble(result);
    }

    public double getPitch() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_get_coordinate", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("location", this),
                Pair.of("type", LocationCoordinate.PITCH)
        ));
        return Unsafe.asDouble(result);
    }
}
