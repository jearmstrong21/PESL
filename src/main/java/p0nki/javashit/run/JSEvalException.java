package p0nki.javashit.run;

import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSStringObject;

import java.util.List;

public class JSEvalException extends Exception {

    private final JSObject object;

    public JSEvalException(String message) {
        this(new JSStringObject(message));
    }

    public JSEvalException(JSObject object) {
        super(object.stringify());
        this.object = object;
    }

    public JSObject getObject() {
        return object;
    }

    public static JSEvalException indexOutOfBounds(int index, int size) {
        return new JSEvalException(String.format("Index out of bounds: %s, for array of length %s", index, size));
    }

    public static int checkIndexOutOfBounds(int index, int size) throws JSEvalException {
        if (index < 0 || index >= size) throw indexOutOfBounds(index, size);
        return index;
    }

    public static JSEvalException cannotSetKey(String value) {
        return new JSEvalException("Cannot set key " + value);
    }

    public static void validateArgumentList(List<JSObject> arguments, int... lengths) throws JSEvalException {
        boolean bad = true;
        for (int i = 0; i < lengths.length && bad; i++) {
            if (arguments.size() == lengths[i]) bad = false;
        }
        if (bad)
            throw new JSEvalException("Invalid argument list");
    }

}
