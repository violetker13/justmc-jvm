package justmc.enums;

import justmc.Primitive;

public enum BooleanEnum implements Primitive {
    TRUE,
    FALSE;

    public static BooleanEnum of(boolean b) {
        return b ? BooleanEnum.TRUE : BooleanEnum.FALSE;
    }
}
