package p0nki.javashit.run;

import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JSContext implements JSMapLike {

    private final JSContext parent;
    private final Map<String, JSObject> objects;

    public JSContext(JSContext parent, Map<String, JSObject> objects) {
        this.parent = parent;
        this.objects = objects;
    }

    @Override
    public JSObject get(String key) throws JSReferenceException {
        if (objects.containsKey(key)) return objects.get(key);
        if (parent != null) return parent.get(key);
        throw new JSReferenceException("Reference exception `" + key + "`");
    }

    @Override
    public void set(String key, JSObject value) {
        objects.put(key, value);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(objects.keySet());
    }

    public JSObject getThis() {
        return JSUndefinedObject.INSTANCE;
    }

}
