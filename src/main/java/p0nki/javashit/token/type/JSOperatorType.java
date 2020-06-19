package p0nki.javashit.token.type;

import p0nki.javashit.object.JSNumberObject;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSStringObject;
import p0nki.javashit.run.JSEvalException;

public enum JSOperatorType {

    ADD((a, b) -> {
        if (a instanceof JSNumberObject && b instanceof JSNumberObject) {
            return new JSNumberObject(((JSNumberObject) a).getValue() + ((JSNumberObject) b).getValue());
        } else {
            return new JSStringObject(a.castToString() + b.castToString());
        }
    }, JSTokenType.ADDITIVE_OP),
    SUB(process((a, b) -> a - b), JSTokenType.ADDITIVE_OP),
    MUL(process((a, b) -> a * b), JSTokenType.MULTIPLICATIVE_OP),
    DIV(process((a, b) -> {
        try {
            return a / b;
        } catch (ArithmeticException ae) {
            throw new JSEvalException("Arithmetic error: " + ae.getMessage());
        }
    }), JSTokenType.MULTIPLICATIVE_OP);

    @FunctionalInterface
    private interface Op {
        JSObject apply(JSObject a, JSObject b) throws JSEvalException;
    }

    @FunctionalInterface
    private interface MathOp {
        double apply(double a, double b) throws JSEvalException;
    }

    private static Op process(MathOp mathOp) {
        return (a, b) -> new JSNumberObject(mathOp.apply(a.asNumber().getValue(), b.asNumber().getValue()));
    }

    private final Op function;
    private final JSTokenType tokenType;

    JSOperatorType(Op function, JSTokenType tokenType) {
        this.function = function;
        this.tokenType = tokenType;
    }

    public JSObject apply(JSObject a, JSObject b) throws JSEvalException {
        return function.apply(a, b);
    }

    public JSTokenType getTokenType() {
        return tokenType;
    }
}
