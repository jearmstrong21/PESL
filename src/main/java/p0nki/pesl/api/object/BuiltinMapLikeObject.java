package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BuiltinMapLikeObject extends PESLObject implements MapLikeObject {

    private final Map<String, PESLObject> values = new HashMap<>();

    public BuiltinMapLikeObject(String type) {
        super(type);
    }

    public static BuiltinMapLikeObject builtinBuilder() {
        return new BuiltinMapLikeObject("map");
    }

    public BuiltinMapLikeObject put(String str, PESLObject object) { // for builders
        values.put(str, object);
        return this;
    }

    @Nonnull
    @Override
    public final PESLObject getKey(@Nonnull String key) {
        return values.getOrDefault(key, UndefinedObject.INSTANCE);
    }

    @Override
    public final void setKey(@Nonnull String key, @Nonnull PESLObject value) throws PESLEvalException {
        throw new PESLEvalException("Cannot set key on this object");
    }

    @Override
    public final boolean containsKey(@Nonnull String key) {
        return getKey(key) != UndefinedObject.INSTANCE;
    }

    @Nonnull
    @Override
    public final Set<String> keys() {
        return Collections.unmodifiableSet(values.keySet());
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "{" +
                values.entrySet().stream().map(entry ->
                        "\"" + entry.getKey() + "\": " + entry.getValue().stringify())
                        .collect(Collectors.joining(", ")) +
                "}";
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return stringify();
    }

    @Override
    public boolean compareEquals(@Nonnull PESLObject object) {
        return false;
    }

}
