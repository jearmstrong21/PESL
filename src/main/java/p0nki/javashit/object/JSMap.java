package p0nki.javashit.object;

import p0nki.javashit.run.JSMapLike;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JSMap extends JSObject implements JSMapLike {

    private final Map<String, JSObject> values;

    public JSMap() {
        values = new HashMap<>();
    }

    public JSMap(Map<String, JSObject> values) {
        this.values = values;
    }

    public JSMap builderSet(String key, JSObject value) {
        this.values.put(key, value);
        return this;
    }

    public Map<String, JSObject> getValues() {
        return values;
    }

    @Override
    public JSObject get(String key) {
        JSObject res = values.getOrDefault(key, JSUndefinedObject.INSTANCE);
        if (res instanceof JSFunction) {
            return new JSFunction(this, ((JSFunction) res).getArgumentNames(), ((JSFunction) res).getNode());
        }
        return res;
    }

    @Override
    public void set(String key, JSObject value) {
        this.values.put(key, value);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(values.keySet());
    }

    @Override
    public String stringify() {
        return "{" +
                values.entrySet().stream().map(entry ->
                        entry.getKey() + ": " + entry.getValue().stringify())
                        .collect(Collectors.joining(", ")) +
                "}";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.MAP;
    }

    @Override
    public String castToString() {
        return stringify();
    }
}
