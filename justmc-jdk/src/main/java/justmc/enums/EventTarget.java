package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class EventTarget extends EnumPrimitive {
    public static final EventTarget DEFAULT = Unsafe.cast(EnumPrimitive.of("default"));
    public static final EventTarget KILLER = Unsafe.cast(EnumPrimitive.of("killer"));
    public static final EventTarget DAMAGER = Unsafe.cast(EnumPrimitive.of("damager"));
    public static final EventTarget VICTIM = Unsafe.cast(EnumPrimitive.of("victim"));
    public static final EventTarget SHOOTER = Unsafe.cast(EnumPrimitive.of("shooter"));
    public static final EventTarget PROJECTILE = Unsafe.cast(EnumPrimitive.of("projectile"));

    private EventTarget() {}
}