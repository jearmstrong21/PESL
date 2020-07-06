package p0nki.pesl.internal.token.type;

import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.BooleanObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.StringObject;

public enum OperatorType {

    ADD((a, b) -> {
        if (a instanceof NumberObject && b instanceof NumberObject) {
            return new NumberObject(((NumberObject) a).getValue() + ((NumberObject) b).getValue());
        } else {
            return new StringObject(a.castToString() + b.castToString());
        }
    }, TokenType.ADDITIVE_OP),
    SUB(processM((a, b) -> a - b), TokenType.ADDITIVE_OP),

    MUL(processM((a, b) -> a * b), TokenType.MULTIPLICATIVE_OP),
    DIV(processM((a, b) -> {
        try {
            return a / b;
        } catch (ArithmeticException ae) {
            throw new PESLEvalException("Arithmetic error: " + ae.getMessage());
        }
    }), TokenType.MULTIPLICATIVE_OP),

    AND(processB((a, b) -> a && b), TokenType.BOOLEAN_OP),
    OR(processB((a, b) -> a || b), TokenType.BOOLEAN_OP),
    XOR(processB((a, b) -> a ^ b), TokenType.BOOLEAN_OP),

    EQUALS((a, b) -> new BooleanObject(a.compareEquals(b)), TokenType.COMPARATIVE_OP),
    LESS_THAN(processMB((a, b) -> a < b), TokenType.COMPARATIVE_OP),
    MORE_THAN(processMB((a, b) -> a > b), TokenType.COMPARATIVE_OP),
    LESS_THAN_OR_EQUAL_TO(processMB((a, b) -> a <= b), TokenType.COMPARATIVE_OP),
    MORE_THAN_OR_EQUAL_TO(processMB((a, b) -> a >= b), TokenType.COMPARATIVE_OP);

    private final Op function;
    private final TokenType tokenType;

    OperatorType(Op function, TokenType tokenType) {
        this.function = function;
        this.tokenType = tokenType;
    }

    private static Op processM(MathOp mathOp) {
        return (a, b) -> new NumberObject(mathOp.apply(a.asNumber().getValue(), b.asNumber().getValue()));
    }

    private static Op processB(BoolOp boolOp) {
        return (a, b) -> new BooleanObject(boolOp.apply(a.asBoolean().getValue(), b.asBoolean().getValue()));
    }

    private static Op processMB(DoubleBoolOp doubleBoolOp) {
        return (a, b) -> new BooleanObject(doubleBoolOp.apply(a.asNumber().getValue(), b.asNumber().getValue()));
    }

    public PESLObject apply(PESLObject a, PESLObject b) throws PESLEvalException {
        return function.apply(a, b);
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    @FunctionalInterface
    private interface Op {
        PESLObject apply(PESLObject a, PESLObject b) throws PESLEvalException;
    }

    @FunctionalInterface
    private interface MathOp {
        double apply(double a, double b) throws PESLEvalException;
    }

    @FunctionalInterface
    private interface BoolOp {
        boolean apply(boolean a, boolean b);
    }

    @FunctionalInterface
    private interface DoubleBoolOp {
        boolean apply(double a, double b);
    }
}
