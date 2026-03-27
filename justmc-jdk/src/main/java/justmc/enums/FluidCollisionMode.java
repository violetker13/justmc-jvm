package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class FluidCollisionMode extends EnumPrimitive {
    public static final FluidCollisionMode NEVER = Unsafe.cast(EnumPrimitive.of("never"));
    public static final FluidCollisionMode SOURCE_ONLY = Unsafe.cast(EnumPrimitive.of("source_only"));
    public static final FluidCollisionMode ALWAYS = Unsafe.cast(EnumPrimitive.of("always"));

    private FluidCollisionMode() {}
}
