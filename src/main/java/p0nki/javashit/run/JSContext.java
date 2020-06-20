package p0nki.javashit.run;

import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSContext implements JSMapLike {

    private JSObject thisValue = JSUndefinedObject.INSTANCE;
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

    public JSContext push() {
        return new JSContext(this, new HashMap<>());
    }

    public void let(String key, JSObject value) {
        objects.put(key, value);
    }

    public void setThis(JSObject thisValue) {
        this.thisValue = thisValue;
    }

    public JSObject getThis() {
        return thisValue;
    }

    @Override
    public void set(String key, JSObject value) {
        JSContext ctx = this;
        while (ctx != null && !ctx.keys().contains(key)) {
            ctx = ctx.parent;
        }
        if (ctx == null) {
            objects.put(key, value);
        } else {
            ctx.objects.put(key, value);
        }
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(objects.keySet());
    }

}
