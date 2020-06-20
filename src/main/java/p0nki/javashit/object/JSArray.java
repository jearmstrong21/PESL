package p0nki.javashit.object;

import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.run.JSMapLike;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JSArray extends JSObject implements JSMapLike {

    private final List<JSObject> values;
    private final JSFunction push;
    private final JSFunction pop;
    private final JSFunction length;

    public JSArray(List<JSObject> values) {
        this.values = values;
        push = JSFunction.of("ARRAY::push", arguments -> {
            JSEvalException.validArgumentListLength(arguments, 1);
            values.add(arguments.get(0));
            return JSUndefinedObject.INSTANCE;
        });
        pop = JSFunction.of("ARRAY::pop", arguments -> {
            JSEvalException.validArgumentListLength(arguments, 0);
            if (values.size() == 0) throw JSEvalException.indexOutOfBounds(-1, 0);
            values.remove(values.size() - 1);
            return JSUndefinedObject.INSTANCE;
        });
        length = JSFunction.of("ARRAY::length", arguments -> {
            JSEvalException.validArgumentListLength(arguments, 0);
            return new JSNumberObject(values.size());
        });
    }

    @Override
    public String stringify() {
        return "[" + values.stream().map(JSObject::stringify).collect(Collectors.joining(", ")) + "]";
    }

    public List<JSObject> getValues() {
        return values;
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.ARRAY;
    }

    @Override
    public String castToString() {
        return stringify();
    }

    @Override
    public JSObject get(String key) throws JSEvalException {
        if (key.equals("push")) return push;
        if (key.equals("pop")) return pop;
        if (key.equals("length")) return length;
        try {
            return values.get(JSEvalException.checkIndexOutOfBounds(Integer.parseInt(key), values.size()));
        } catch (NumberFormatException nfe) {
            return JSUndefinedObject.INSTANCE;
        }
    }

    @Override
    public void set(String key, JSObject value) throws JSEvalException {
        try {
            values.set(JSEvalException.checkIndexOutOfBounds(Integer.parseInt(key), values.size()), value);
        } catch (NumberFormatException nfe) {
            throw JSEvalException.cannotSetKey(key);
        }
    }

    @Override
    public Set<String> keys() {
        Set<String> keys = IntStream.range(0, values.size()).boxed().map(x -> x + "").collect(Collectors.toSet());
        keys.add("push");
        keys.add("pop");
        keys.add("length");
        return Collections.unmodifiableSet(keys);
    }
}
