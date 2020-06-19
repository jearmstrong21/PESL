package p0nki.javashit.object;

import p0nki.javashit.run.JSMapLike;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JSMap extends JSObject implements JSMapLike {

    private final Map<String, JSObject> map;

    public JSMap() {
        map = new HashMap<>();
    }

    public JSMap(Map<String, JSObject> map) {
        this.map = map;
    }

    public JSMap builderSet(String key, JSObject value) {
        map.put(key, value);
        return this;
    }

    @Override
    public JSObject get(String key) {
        return map.getOrDefault(key, JSUndefinedObject.INSTANCE);
    }

    @Override
    public void set(String key, JSObject value) {
        map.put(key, value);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public String stringify() {
        return "{" +
                map.entrySet().stream().map(entry ->
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
