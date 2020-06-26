package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.Nonnull;

public abstract class PESLObject {

    @Nonnull
    public abstract String stringify();

    @Nonnull
    public abstract ObjectType type();

    @Nonnull
    public abstract String castToString();

    @Nonnull
    @Override
    public final String toString() {
        return stringify();
    }

    @Nonnull
    public final NumberObject asNumber() throws PESLEvalException {
        if (this instanceof NumberObject) return (NumberObject) this;
        throw new PESLEvalException("Cannot cast " + type().toString() + " to NUMBER");
    }

    @Nonnull
    public final BooleanObject asBoolean() throws PESLEvalException {
        if (this instanceof BooleanObject) return (BooleanObject) this;
        throw new PESLEvalException("Cannot cast " + type().toString() + " to BOOLEAN");
    }

    @Nonnull
    public final MapLikeObject asMapLike() throws PESLEvalException {
        if (this instanceof MapLikeObject) return (MapLikeObject) this;
        throw new PESLEvalException("Cannot cast " + type().toString() + " to [maplike]");
    }

    @Nonnull
    public final StringObject asString() throws PESLEvalException {
        if (this instanceof StringObject) return (StringObject) this;
        throw new PESLEvalException("Cannot cast " + type().toString() + " to STRING");
    }

    @Nonnull
    public final FunctionObject asFunction() throws PESLEvalException {
        if (this instanceof FunctionObject) return (FunctionObject) this;
        throw new PESLEvalException("Cannot cast " + type().toString() + " to FUNCTION");
    }

    @Nonnull
    public final ArrayObject asArray() throws PESLEvalException {
        if (this instanceof ArrayObject) return (ArrayObject) this;
        throw new PESLEvalException("Cannot cast " + type().toString() + " to ARRAY");
    }

}
