package justmc;

import justmc.annotation.Inline;
import justmc.entity.*;
import justmc.enums.EventTarget;
import justmc.enums.SelectionMode;

@Inline
public final class Selection {
    private Selection() {}

    /**
     * @return Текущая цель
     */
    public static native AbstractEntity getCurrent();

    /**
     * @return Цель события
     */
    public static native AbstractEntity getDefault();

    /**
     * @return Сущность по умолчанию
     */
    public static native Entity getDefaultEntity();

    /**
     * @return Убийца события
     */
    public static native AbstractEntity getKiller();

    /**
     * @return Атакующий события
     */
    public static native AbstractEntity getDamager();

    /**
     * @return Стрелок события
     */
    public static native AbstractEntity getShooter();

    /**
     * @return Снаряд события
     */
    public static native Projectile getProjectile();

    /**
     * @return Жертва события
     */
    public static native AbstractEntity getVictim();

    /**
     * @return Случайная загруженная сущность
     */
    public static native Entity getRandomEntity();

    /**
     * @return Случайный игрок
     */
    public static native Player getRandomPlayer();

    /**
     * @return Все игроки
     */
    public static native Player getAllPlayers();

    /**
     * @return Все загруженные мобы
     */
    public static native Mob getAllMobs();

    /**
     * @return Все загруженные сущности
     */
    public static native Entity getAllEntities();

    /**
     * @return Последняя созданная сущность
     */
    public static native Entity getLastEntity();

    public static void reset() {
        Unsafe.operation("select_reset", MapPrimitive.empty());
    }

    public static void selectEventTarget(EventTarget target) {
        Unsafe.operation("select_event_target", MapPrimitive.of(
                Pair.of("selection_type", target)
        ));
    }

    public static void selectRandomPlayer() {
        Unsafe.operation("select_event_target", MapPrimitive.empty());
    }

    public static void selectPlayerByName(Primitive names) {
        Unsafe.operation("select_player_by_name", MapPrimitive.of(
                Pair.of("name_or_uuid", names)
        ));
    }

    public static void selectAllPlayers() {
        Unsafe.operation("select_all_players", MapPrimitive.empty());
    }

    public static void selectAllMobs() {
        Unsafe.operation("select_all_mobs", MapPrimitive.empty());
    }

    public static void selectAllEntities() {
        Unsafe.operation("select_all_entities", MapPrimitive.empty());
    }

    public static int getTargetsAmount() {
        return Unsafe.cast(World.getValue("selection_size"));
    }

    public static ListPrimitive<Text> getTargetsNames() {
        return Unsafe.cast(World.getValue("selection_target_names"));
    }

    public static ListPrimitive<Text> getTargetsUUIDs() {
        return Unsafe.cast(World.getValue("selection_target_uuids"));
    }

    public static void filterBy(Conditional filter) {
        Unsafe.operation("select_filter_by_conditional", filter);
    }

    public static void isolated(Runnable block) {
        Unsafe.operation("controller_isolated_selection", MapPrimitive.empty(), block);
    }

    public static void isolated(SelectionMode selection, Runnable block) {
        Unsafe.operation("controller_isolated_selection", MapPrimitive.of(
            Pair.of("selection_mode", selection)
        ), block);
    }
}
