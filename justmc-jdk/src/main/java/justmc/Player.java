package justmc;

import justmc.annotation.Inline;
import justmc.enums.TextMerging;

@Inline
public enum Player {
    CURRENT,
    DEFAULT,
    KILLER,
    DAMAGER,
    SHOOTER,
    VICTIM,
    RANDOM,
    ALL;

    public final native void operation(String id, CopyableMap<Text, Primitive> args);

    public final void sendMessage(String message) {
        operation("player_send_message", CopyableMap.of(
                Pair.of("messages", Text.legacy(message))
        ));
    }

    public final void sendMessage(Text[] messages, TextMerging merging) {
        operation("player_send_message", CopyableMap.of(
                Pair.of("messages", CopyableList.of(messages)),
                Pair.of("merging", Unsafe.cast(merging))
        ));
    }

    public final Location getLocation() {
        return Unsafe.cast(GameValue.ofPlayer(this).getValue("location"));
    }

    public final double getX() {
        return Unsafe.asDouble(GameValue.ofPlayer(this).getValue("x_coordinate"));
    }

    public final double getY() {
        return Unsafe.asDouble(GameValue.ofPlayer(this).getValue("y_coordinate"));
    }

    public final double getZ() {
        return Unsafe.asDouble(GameValue.ofPlayer(this).getValue("z_coordinate"));
    }

    public final Location getTargetBlock() {
        return Unsafe.cast(GameValue.ofPlayer(this).getValue("target_block"));
    }
}