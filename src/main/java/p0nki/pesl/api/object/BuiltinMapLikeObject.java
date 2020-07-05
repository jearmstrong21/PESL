package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class BuiltinMapLikeObject extends PESLObject implements MapLikeObject {

    private final Map<String, PESLObject> map = new HashMap<>();

    protected BuiltinMapLikeObject(String type) {
        super(type);
    }

    protected void put(String str, PESLObject object) {
        map.put(str, object);
    }

    @Nonnull
    @Override
    public final PESLObject getKey(@Nonnull String key) {
        return map.getOrDefault(key, UndefinedObject.INSTANCE);
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
        return Collections.unmodifiableSet(map.keySet());
    }

}
