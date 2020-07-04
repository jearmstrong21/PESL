package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public abstract class PESLObject {

    private final String type;

    protected PESLObject(String type) {
        this.type = type;
    }

    @CheckReturnValue
    public final String getType() {
        return type;
    }

    @Nonnull
    @CheckReturnValue
    public abstract String stringify();

    @Nonnull
    @CheckReturnValue
    public abstract String castToString();

//    public abstract boolean compareEquals(@Nonnull PESLObject object);

    @Nonnull
    @Override
    public final String toString() {
        return stringify();
    }

    @Nonnull
    @CheckReturnValue
    public final NumberObject asNumber() throws PESLEvalException {
        if (this instanceof NumberObject) return (NumberObject) this;
        throw new PESLEvalException("Cannot cast " + getType() + " to number");
    }

    @Nonnull
    @CheckReturnValue
    public final BooleanObject asBoolean() throws PESLEvalException {
        if (this instanceof BooleanObject) return (BooleanObject) this;
        throw new PESLEvalException("Cannot cast " + getType() + " to boolean");
    }

    @Nonnull
    @CheckReturnValue
    public final MapLikeObject asMapLike() throws PESLEvalException {
        if (this instanceof MapLikeObject) return (MapLikeObject) this;
        throw new PESLEvalException("Cannot cast " + getType() + " to maplike");
    }

    @Nonnull
    @CheckReturnValue
    public final StringObject asString() throws PESLEvalException {
        if (this instanceof StringObject) return (StringObject) this;
        throw new PESLEvalException("Cannot cast " + getType() + " to string");
    }

    @Nonnull
    @CheckReturnValue
    public final FunctionObject asFunction() throws PESLEvalException {
        if (this instanceof FunctionObject) return (FunctionObject) this;
        throw new PESLEvalException("Cannot cast " + getType() + " to function");
    }

    @Nonnull
    @CheckReturnValue
    public final ArrayObject asArray() throws PESLEvalException {
        if (this instanceof ArrayObject) return (ArrayObject) this;
        throw new PESLEvalException("Cannot cast " + getType() + " to array");
    }

}
