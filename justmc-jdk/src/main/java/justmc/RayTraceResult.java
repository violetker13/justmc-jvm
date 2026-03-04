package justmc;

import justmc.annotation.Inline;
import justmc.enums.BlockFace;
import org.jetbrains.annotations.Nullable;

@Inline
public final class RayTraceResult implements Primitive {
    private RayTraceResult() {}

    public static RayTraceResult of(
            @Nullable Location location,
            @Nullable Location blockLocation,
            @Nullable BlockFace face,
            @Nullable String entity
    ) {
        return Unsafe.typed(CopyableList.of(location, blockLocation, Unsafe.typed(face), Text.plain(entity)));
    }

    public native @Nullable Location getLocation();
    public native @Nullable Location getBlockLocation();
    public native @Nullable BlockFace getFace();
    public native @Nullable String getEntity();
}
