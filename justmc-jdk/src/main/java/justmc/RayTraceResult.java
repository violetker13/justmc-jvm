package justmc;

import justmc.annotation.Inline;
import justmc.enums.BlockFace;
import org.jetbrains.annotations.Nullable;

@Inline
public final class RayTraceResult extends Primitive {
    private RayTraceResult() {}

    public static RayTraceResult of(
            @Nullable Location location,
            @Nullable Location blockLocation,
            @Nullable BlockFace face,
            @Nullable Text entity
    ) {
        return Unsafe.cast(ListPrimitive.of(location, blockLocation, face, entity));
    }

    public @Nullable Location getLocation() {
        return Unsafe.cast(Unsafe.<ListPrimitive<?>>cast(this).get(0));
    }

    public @Nullable Location getBlockLocation() {
        return Unsafe.cast(Unsafe.<ListPrimitive<?>>cast(this).get(1));
    }

    public @Nullable BlockFace getFace() {
        return Unsafe.cast(Unsafe.<ListPrimitive<?>>cast(this).get(2));
    }

    public @Nullable Text getEntity() {
        return Unsafe.cast(Unsafe.<ListPrimitive<?>>cast(this).get(3));
    }
}
