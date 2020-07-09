package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class NumberObject extends PESLObject {

    private final double value;

    public static final String TYPE = "number";

    public NumberObject(double value) {
        super(TYPE);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return value + "";
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return value + "";
    }

    @Override
    public boolean compareEquals(@Nonnull PESLObject object) {
        return object instanceof NumberObject && value == ((NumberObject) object).getValue();
    }

}
