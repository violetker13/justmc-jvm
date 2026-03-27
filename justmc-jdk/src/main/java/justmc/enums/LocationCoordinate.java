package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class LocationCoordinate extends EnumPrimitive {
    public static final LocationCoordinate X = Unsafe.cast(EnumPrimitive.of("x"));
    public static final LocationCoordinate Y = Unsafe.cast(EnumPrimitive.of("y"));
    public static final LocationCoordinate Z = Unsafe.cast(EnumPrimitive.of("z"));
    public static final LocationCoordinate YAW = Unsafe.cast(EnumPrimitive.of("yaw"));
    public static final LocationCoordinate PITCH = Unsafe.cast(EnumPrimitive.of("pitch"));

    private LocationCoordinate() {}
}
