package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    @Override
    public @javax.annotation.Nonnull
    PESLObject get(String key) {
        PESLObject res = values.getOrDefault(key, UndefinedObject.INSTANCE);
        if (res instanceof FunctionObject) {
            return new FunctionObject(this, ((FunctionObject) res).getArgumentNames(), ((FunctionObject) res).getNode());
        }
        return res;
    }

    @Override
    public void set(@Nonnull String key, @Nonnull PESLObject value) {
        this.values.put(key, value);
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
}
