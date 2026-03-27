package override.java.lang;

import justmc.Unsafe;

public class Object {
    public Object() {}

    public boolean equals(java.lang.Object obj) {
        return this == obj;
    }

    public java.lang.String toString() {
        return getClass().getName() + "@" + java.lang.Integer.toHexString(Unsafe.asAddress(this));
    }

    public int hashCode() {
        return Unsafe.asAddress(this);
    }
}
