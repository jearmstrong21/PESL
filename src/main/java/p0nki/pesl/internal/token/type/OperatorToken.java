package p0nki.pesl.internal.token.type;

public class OperatorToken extends PESLToken {

    private final BiOperatorType opType;

    public OperatorToken(BiOperatorType opType, int start, int end) {
        super(opType.getTokenType(), start, end);
        this.opType = opType;
    }

    public BiOperatorType getOpType() {
        return opType;
    }

    @Override
    public String toString() {
        return "OPERATOR[" + getType() + "," + opType + "]";
    }
}
