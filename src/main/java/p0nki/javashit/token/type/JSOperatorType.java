package p0nki.javashit.token.type;

import p0nki.javashit.object.JSBooleanObject;
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
    SUB(processM((a, b) -> a - b), JSTokenType.ADDITIVE_OP),

    MUL(processM((a, b) -> a * b), JSTokenType.MULTIPLICATIVE_OP),
    DIV(processM((a, b) -> {
        try {
            return a / b;
        } catch (ArithmeticException ae) {
            throw new JSEvalException("Arithmetic error: " + ae.getMessage());
        }
    }), JSTokenType.MULTIPLICATIVE_OP),

    AND(processB((a, b) -> a && b), JSTokenType.BOOLEAN_OP),
    OR(processB((a, b) -> a || b), JSTokenType.BOOLEAN_OP),
    XOR(processB((a, b) -> a ^ b), JSTokenType.BOOLEAN_OP),

    EQUALS((a, b) -> new JSBooleanObject(a.equals(b)), JSTokenType.COMPARATIVE_OP),
    LESS_THAN(processMB((a, b) -> a < b), JSTokenType.COMPARATIVE_OP),
    MORE_THAN(processMB((a, b) -> a > b), JSTokenType.COMPARATIVE_OP);

    @FunctionalInterface
    private interface Op {
        JSObject apply(JSObject a, JSObject b) throws JSEvalException;
    }

    @FunctionalInterface
    private interface MathOp {
        double apply(double a, double b) throws JSEvalException;
    }

    @FunctionalInterface
    private interface BoolOp {
        boolean apply(boolean a, boolean b) throws JSEvalException;
    }

    @FunctionalInterface
    private interface DoubleBoolOp {
        boolean apply(double a, double b) throws JSEvalException;
    }

    private static Op processM(MathOp mathOp) {
        return (a, b) -> new JSNumberObject(mathOp.apply(a.asNumber().getValue(), b.asNumber().getValue()));
    }

    private static Op processB(BoolOp boolOp) {
        return (a, b) -> new JSBooleanObject(boolOp.apply(a.asBoolean().getValue(), b.asBoolean().getValue()));
    }

    private static Op processMB(DoubleBoolOp doubleBoolOp) {
        return (a, b) -> new JSBooleanObject(doubleBoolOp.apply(a.asNumber().getValue(), b.asNumber().getValue()));
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
