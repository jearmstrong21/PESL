package p0nki.javashit.object;

import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.run.JSMapLike;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JSStringObject extends JSObject implements JSMapLike {

    private final String value;
    private final JSFunction charAt;
    private final JSFunction substring;
    private final JSFunction length;

    public static final Set<String> KEYS = Collections.unmodifiableSet(new HashSet<String>() {{
        add("charAt");
        add("substring");
        add("length");
    }});


    public JSStringObject(String value) {
        this.value = value;
        charAt = JSFunction.of(false, arguments -> {
            JSEvalException.validArgumentListLength(arguments, 1);
            return new JSStringObject("" + value.charAt((int) arguments.get(0).asNumber().getValue()));
        });
        substring = JSFunction.of(false, arguments -> {
            JSEvalException.validArgumentListLength(arguments, 1, 2);
            int beginIndex = (int) arguments.get(0).asNumber().getValue();
            JSEvalException.checkIndexOutOfBounds(beginIndex, value.length());
            if (arguments.size() == 1) return new JSStringObject(value.substring(beginIndex));
            int endIndex = (int) arguments.get(1).asNumber().getValue();
            JSEvalException.checkIndexOutOfBounds(endIndex, value.length());
            if (beginIndex > endIndex) throw JSEvalException.indexOutOfBounds(beginIndex, value.length());
            return new JSStringObject(value.substring(beginIndex, endIndex));
        });
        length = JSFunction.of(false, arguments -> {
            JSEvalException.validArgumentListLength(arguments, 0);
            return new JSNumberObject(value.length());
        });
    }

    public String getValue() {
        return value;
    }

    @Override
    public String stringify() {
        return "\"" + value + "\"";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.STRING;
    }

    @Override
    public String castToString() {
        return value;
    }

    @Override
    public JSObject get(String key) {
        if (key.equals("charAt")) return charAt;
        if (key.equals("substring")) return substring;
        return JSUndefinedObject.INSTANCE;
    }

    @Override
    public void set(String key, JSObject value) throws JSEvalException {
        throw JSEvalException.cannotSetKey(key);
    }

    @Override
    public Set<String> keys() {
        return KEYS;
    }
}
