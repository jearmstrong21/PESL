package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class BooleanObject extends PESLObject {

    private final boolean value;

    public BooleanObject(boolean value) {
        super("boolean");
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "" + value;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return stringify();
    }
}
