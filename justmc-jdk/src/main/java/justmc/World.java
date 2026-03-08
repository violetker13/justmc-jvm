package justmc;

import justmc.annotation.Inline;
import justmc.enums.FluidCollisionMode;
import justmc.enums.RayCollisionMode;

@Inline
public final class World {
    private World() {}

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
            ListPrimitive<Text> entities
    ) {
        var hitLocation = Variable.temp();
        var hitBlockLocation = Variable.temp();
        var hitBlockFace = Variable.temp();
        var hitEntityUUID = Variable.temp();
        Unsafe.operation("set_variable_ray_trace_result", MapPrimitive.of(
                Pair.of("start", start),
                Pair.of("ray_size", NumberPrimitive.of(raySize)),
                Pair.of("max_distance", NumberPrimitive.of(maxDistance)),
                Pair.of("ray_collision_mode", EnumPrimitive.of(rayCollisionMode)),
                Pair.of("ignore_passable_blocks", EnumPrimitive.of(ignorePassableBlocks)),
                Pair.of("fluid_collision_mode", Unsafe.cast(fluidCollisionMode)),
                Pair.of("variable_for_hit_location", hitLocation),
                Pair.of("variable_for_hit_block_location", hitBlockLocation),
                Pair.of("variable_for_hit_block_face", hitBlockFace),
                Pair.of("variable_for_hit_entity_uuid", hitEntityUUID),
                Pair.of("entities", entities)
        ));
        return RayTraceResult.of(
                Unsafe.cast(hitLocation),
                Unsafe.cast(hitBlockLocation),
                Unsafe.cast(hitBlockFace),
                Unsafe.cast(hitEntityUUID)
        );
    }
}
