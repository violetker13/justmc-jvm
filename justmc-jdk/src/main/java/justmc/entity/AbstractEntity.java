package justmc.entity;

import justmc.*;
import justmc.enums.NbtValueType;

/**
 * Самая абстрактная сущность, которая может быть игроком
 */
public interface AbstractEntity {
    Primitive getValue(String id);

    void operation(String id, MapPrimitive<Text, Primitive> args);

    default Text getName() {
        return Unsafe.cast(getValue("name"));
    }

    default Location getLocation() {
        return Unsafe.cast(getValue("location"));
    }

    default double getX() {
        return Unsafe.asDouble(getValue("x_coordinate"));
    }

    default double getY() {
        return Unsafe.asDouble(getValue("y_coordinate"));
    }

    default double getZ() {
        return Unsafe.asDouble(getValue("z_coordinate"));
    }

    default Primitive getNbt(ListPrimitive<Text> path, NbtValueType type) {
        var result = Variable.result();
        operation("entity_get_nbt", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("path", path),
                Pair.of("value_type", type)
        ));
        return result;
    }
}
