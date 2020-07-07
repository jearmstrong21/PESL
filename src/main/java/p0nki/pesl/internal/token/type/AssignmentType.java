package p0nki.pesl.internal.token.type;

import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;

public enum AssignmentType {

    EQUALS((a, b) -> b),
    PLUS_EQUALS(BiOperatorType.ADD::apply),
    MINUS_EQUALS(BiOperatorType.SUB::apply),
    TIMES_EQUALS(BiOperatorType.MUL::apply),
    DIVIDES_EQUALS(BiOperatorType.DIV::apply);
    // TODO modulo operator?

    private final Op function;

    AssignmentType(Op function) {
        this.function = function;
    }

    public PESLObject apply(PESLObject a, PESLObject b) throws PESLEvalException {
        return function.apply(a, b);
    }

    @FunctionalInterface
    private interface Op {

        PESLObject apply(PESLObject a, PESLObject b) throws PESLEvalException;

    }
}
