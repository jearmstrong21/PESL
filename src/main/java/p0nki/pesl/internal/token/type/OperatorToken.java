package p0nki.pesl.internal.token.type;

public class OperatorToken extends PESLToken {

    private final OperatorType opType;

    public OperatorToken(OperatorType opType, int start, int end) {
        super(opType.getTokenType(), start, end);
        this.opType = opType;
    }

    public OperatorType getOpType() {
        return opType;
    }

    @Override
    public String toString() {
        return "OPERATOR[" + getType() + "," + opType + "]";
    }
}
