package p0nki.javashit.object;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.ast.nodes.JSASTNode;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.run.JSMapLike;

import java.util.ArrayList;
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
        push = new JSFunction(new ArrayList<String>() {{
            add("value");
        }}, new JSASTNode() {
            @Override
            public JSObject evaluate(JSContext context) throws JSEvalException {
                JSObject value = context.get("value");
                values.add(value);
                return value;
            }

            @Override
            public void print(IndentedLogger logger) {
                logger.println("ARRAY::PUSH");
            }
        });
        pop = new JSFunction(new ArrayList<>(), new JSASTNode() {
            @Override
            public JSObject evaluate(JSContext context) throws JSEvalException {
                if (values.size() == 0) throw JSEvalException.arrayIndexOutOfBounds(-1, 0);
                return values.remove(values.size() - 1);
            }

            @Override
            public void print(IndentedLogger logger) {
                logger.println("ARRAY::POP");
            }
        });
        length = new JSFunction(new ArrayList<>(), new JSASTNode() {
            @Override
            public JSObject evaluate(JSContext context) {
                return new JSNumberObject(values.size());
            }

            @Override
            public void print(IndentedLogger logger) {
                logger.println("ARRAY::LENGTH");
            }
        });
    }

    @Override
    public String stringify() {
        return "[" + values.stream().map(JSObject::stringify).collect(Collectors.joining(", ")) + "]";
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
            int i = Integer.parseInt(key);
            if (i < 0 || i >= values.size()) throw JSEvalException.arrayIndexOutOfBounds(i, values.size());
            return values.get(i);
        } catch (NumberFormatException nfe) {
            throw JSEvalException.expectedNumber(key);
        }
    }

    @Override
    public void set(String key, JSObject value) throws JSEvalException {
        try {
            int i = Integer.parseInt(key);
            if (i < 0 || i >= values.size()) throw JSEvalException.arrayIndexOutOfBounds(i, values.size());
            values.set(i, value);
        } catch (NumberFormatException nfe) {
            throw JSEvalException.expectedNumber(key);
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
