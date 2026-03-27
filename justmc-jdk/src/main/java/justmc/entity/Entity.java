package justmc.entity;

import justmc.MapPrimitive;

/**
 * Абстрактная сущность, которая <u>не</u> может быть игроком
 */
public interface Entity extends AbstractEntity {
    default void remove() {
        operation("entity_remove", MapPrimitive.empty());
    }
}
