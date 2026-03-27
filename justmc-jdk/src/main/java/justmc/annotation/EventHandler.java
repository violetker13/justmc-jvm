package justmc.annotation;

/**
 * Аннотация для static методов, которые будут заменены на событие.
 * <a href="https://github.com/donzgold/JustMC_compilator/blob/master/data/events.json">Список событий</a>
 */
public @interface EventHandler {
    String id() default "";
}
