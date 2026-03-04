package justmc;

import justmc.annotation.Inline;
import justmc.enums.BooleanEnum;
import justmc.enums.FluidCollisionMode;
import justmc.enums.RayCollisionMode;

@Inline
public final class World {
    private World() {}

    public static long currentTimeMillis() {
        return Unsafe.asInt(GameValue.get("timestamp"));
    }

    public static int getCpu() {
        return Unsafe.asInt(GameValue.get("cpu_usage"));
    }

    public static RayTraceResult rayTrace(
            Location start,
            double raySize,
            double maxDistance,
            RayCollisionMode rayCollisionMode,
            boolean ignorePassableBlocks,
            FluidCollisionMode fluidCollisionMode,
            CopyableList<Text> entities
    ) {
        var hitLocation = Variable.temp();
        var hitBlockLocation = Variable.temp();
        var hitBlockFace = Variable.temp();
        var hitEntityUUID = Variable.temp();
        Unsafe.operation("set_variable_ray_trace_result", CopyableMap.of(
                Pair.of("start", start),
                Pair.of("ray_size", NumberPrimitive.of(raySize)),
                Pair.of("max_distance", NumberPrimitive.of(maxDistance)),
                Pair.of("ray_collision_mode", rayCollisionMode),
                Pair.of("ignore_passable_blocks", BooleanEnum.of(ignorePassableBlocks)),
                Pair.of("fluid_collision_mode", Unsafe.typed(fluidCollisionMode)),
                Pair.of("variable_for_hit_location", hitLocation),
                Pair.of("variable_for_hit_block_location", hitBlockLocation),
                Pair.of("variable_for_hit_block_face", hitBlockFace),
                Pair.of("variable_for_hit_entity_uuid", hitEntityUUID),
                Pair.of("entities", entities)
        ));
        return RayTraceResult.of(
                Unsafe.asLocation(hitLocation),
                Unsafe.asLocation(hitBlockLocation),
                Unsafe.asEnum(hitBlockFace),
                Unsafe.asString(hitEntityUUID)
        );
    }
}
