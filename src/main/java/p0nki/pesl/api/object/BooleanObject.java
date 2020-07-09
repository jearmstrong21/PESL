package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class BooleanObject extends PESLObject {

    public static final String TYPE = "boolean";

    private final boolean value;

    public BooleanObject(boolean value) {
        super(TYPE);
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

    @Override
    public boolean compareEquals(@Nonnull PESLObject object) {
        return object instanceof BooleanObject && value == ((BooleanObject) object).getValue();
    }
}
