package p0nki.javashit.object;

import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.run.JSMapLike;

public abstract class JSObject {

    public abstract String stringify();

    public abstract JSObjectType type();

    @Override
    public final String toString() {
        return stringify();
    }

    public final JSNumberObject asNumber() throws JSEvalException {
        if (this instanceof JSNumberObject) return (JSNumberObject) this;
        throw new JSEvalException("Cannot cast " + type().toString() + " to NUMBER");
    }

    public final JSMapLike asMapLike() throws JSEvalException {
        if (this instanceof JSMapLike) return (JSMapLike) this;
        throw new JSEvalException("Cannot cast " + type().toString() + " to [maplike]");
    }

    public final JSStringObject asString() throws JSEvalException {
        if (this instanceof JSStringObject) return (JSStringObject) this;
        throw new JSEvalException("Cannot cast " + type().toString() + " to STRING");
    }

}
