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
        return Unsafe.cast(ListPrimitive.of(location, blockLocation, Unsafe.cast(face), entity));
    }

    public native @Nullable Location getLocation();
    public native @Nullable Location getBlockLocation();
    public native @Nullable BlockFace getFace();
    public native @Nullable Text getEntity();
}
