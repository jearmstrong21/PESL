package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class MapObject extends PESLObject implements MapLikeObject {

    private final Map<String, PESLObject> values;

    public MapObject() {
        this(new HashMap<>());
    }

    public MapObject(Map<String, PESLObject> values) {
        super("map");
        this.values = values;
    }

    public MapObject builderSet(String key, PESLObject value) {
        this.values.put(key, value);
        return this;
    }

    public Map<String, PESLObject> getValues() {
        return values;
    }

    @Nonnull
    @Override
    public PESLObject getKey(@Nonnull String key) {
        PESLObject res = values.getOrDefault(key, UndefinedObject.INSTANCE);
        if (res instanceof FunctionObject) {
            return new FunctionObject(this, ((FunctionObject) res).getArgumentNames(), ((FunctionObject) res).getNode());
        }
        return res;
    }

    @Override
    public PESLObject setKey(@Nonnull String key, @Nonnull PESLObject value) {
        if (value == UndefinedObject.INSTANCE)
            return Optional.ofNullable(values.remove(key)).orElse(UndefinedObject.INSTANCE);
        else return this.values.put(key, value);
    }

    @Override
    public boolean containsKey(@Nonnull String key) {
        return getKey(key) != UndefinedObject.INSTANCE;
    }

    @Override
    public @Nonnull
    Set<String> keys() {
        return Collections.unmodifiableSet(values.keySet());
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "{" +
                values.entrySet().stream().map(entry ->
                        entry.getKey() + ": " + entry.getValue().stringify())
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
    public boolean compareEquals(@Nonnull PESLObject object) throws PESLEvalException {
        return false;
    }
}
