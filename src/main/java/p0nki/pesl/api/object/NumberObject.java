package p0nki.pesl.api.object;

import javax.annotation.Nonnull;

public class NumberObject extends PESLObject {

    private final double value;

    public NumberObject(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Nonnull
    @Override
    public String stringify() {
        return value + "";
    }

    @Nonnull
    @Override
    public ObjectType type() {
        return ObjectType.NUMBER;
    }

    @Nonnull
    @Override
    public String castToString() {
        return value + "";
    }

}
