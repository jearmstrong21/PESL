package p0nki.pesl.api;

import p0nki.pesl.api.builtins.PESLBuiltins;
import p0nki.pesl.api.object.MapLikeObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PESLContext implements MapLikeObject {

    private final PESLContext parent;
    private final Map<String, PESLObject> objects;
    private PESLObject thisValue = UndefinedObject.INSTANCE;

    public PESLContext() {
        this.parent = null;
        objects = new HashMap<>();
        objects.put("range", PESLBuiltins.RANGE);
        objects.put("typeof", PESLBuiltins.TYPEOF);
        objects.put("dir", PESLBuiltins.DIR);
        objects.put("Math", PESLBuiltins.MATH);
        objects.put("Data", PESLBuiltins.DATA);
        objects.put("parseNumber", PESLBuiltins.PARSE_NUMBER);
        objects.put("System", PESLBuiltins.SYSTEM);
    }

    public PESLContext(Map<String, PESLObject> objects) {
        this();
        this.objects.putAll(objects);
    }

    private PESLContext(@Nonnull PESLContext parent, Map<String, PESLObject> objects) {
        this.parent = Objects.requireNonNull(parent);
        this.objects = objects;
    }

    @Nonnull
    @Override
    public PESLObject getKey(@Nonnull String key) {
        if (objects.containsKey(key)) return objects.get(key);
        if (parent != null) return parent.getKey(key);
        return UndefinedObject.INSTANCE;
    }

    @Nonnull
    public PESLContext push() {
        return new PESLContext(this, new HashMap<>());
    }

    public void let(@Nonnull String key, @Nonnull PESLObject value) {
        objects.put(key, value);
    }

    @Nullable
    public PESLObject getThis() {
        return thisValue;
    }

    public void setThis(@Nullable PESLObject thisValue) {
        this.thisValue = thisValue;
    }

    @Override
    public void setKey(@Nonnull String key, @Nonnull PESLObject value) {
        if (parent == null) {
            objects.put(key, value);
        } else {
            if (objects.getOrDefault(key, UndefinedObject.INSTANCE) == UndefinedObject.INSTANCE) {
                parent.setKey(key, value);
            } else {
                objects.put(key, value);
            }
        }
    }

    @Override
    public boolean containsKey(@Nonnull String key) {
        return getKey(key) != UndefinedObject.INSTANCE;
    }

    @Override
    public @Nonnull
    Set<String> keys() {
        return Collections.unmodifiableSet(objects.keySet());
    }

}
