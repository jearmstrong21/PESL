package p0nki.javashit.run;

import p0nki.javashit.object.JSObject;

import java.util.Set;

public interface JSMapLike {

    JSObject get(String key) throws JSReferenceException;

    void set(String key, JSObject value);

    Set<String> keys();

}
