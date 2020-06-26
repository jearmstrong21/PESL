package p0nki.pesl.api.object;

import javax.annotation.Nonnull;

public class BooleanObject extends PESLObject {

    private final boolean value;

    public BooleanObject(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Nonnull
    @Override
    public String stringify() {
        return "" + value;
    }

    @Nonnull
    @Override
    public ObjectType type() {
        return ObjectType.BOOLEAN;
    }

    @Nonnull
    @Override
    public String castToString() {
        return stringify();
    }
}
