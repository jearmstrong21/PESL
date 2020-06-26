package p0nki.pesl.internal;

import p0nki.pesl.api.object.PESLObject;

public class InternalReturnException extends RuntimeException {

    private final PESLObject value;

    public InternalReturnException(PESLObject value) {
        super("Unnecessary return");
        this.value = value;
    }

    public PESLObject getValue() {
        return value;
    }
}
