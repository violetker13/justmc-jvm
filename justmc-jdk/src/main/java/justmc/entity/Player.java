package justmc.entity;

import justmc.*;
import justmc.enums.NbtValueType;
import justmc.enums.TextMerging;

public interface Player extends LivingEntity {
    default void sendMessage(String message) {
        operation("player_send_message", MapPrimitive.of(
                Pair.of("messages", Text.legacy(message))
        ));
    }

    default void sendMessage(Primitive messages, TextMerging merging) {
        operation("player_send_message", MapPrimitive.of(
                Pair.of("messages", messages),
                Pair.of("merging", merging)
        ));
    }

    default boolean isSneaking() {
        return Conditional.of("if_player_is_sneaking").get();
    }

    @Override
    default Primitive getNbt(ListPrimitive<Text> path, NbtValueType type) {
        var result = Variable.result();
        operation("player_get_nbt", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("path", path),
                Pair.of("value_type", type)
        ));
        return result;
    }
}