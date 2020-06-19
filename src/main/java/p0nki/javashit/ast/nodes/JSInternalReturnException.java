package p0nki.javashit.ast.nodes;

import p0nki.javashit.object.JSObject;

public class JSInternalReturnException extends RuntimeException {

    private final JSObject value;

    public JSInternalReturnException(JSObject value) {
        super("Unnecessary return");
        this.value = value;
    }

    public JSObject getValue() {
        return value;
    }
}
