package justmc;

import justmc.annotation.Inline;

@Inline
public final class Vector extends Primitive {
    private Vector() {}

    public static native Vector of(double x, double y, double z);

    public native double getX();
    public native double getY();
    public native double getZ();
}
