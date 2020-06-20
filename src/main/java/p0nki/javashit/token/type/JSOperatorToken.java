package p0nki.javashit.token.type;

public class JSOperatorToken extends JSToken {

    private final JSOperatorType opType;

    public JSOperatorToken(JSOperatorType opType, int start, int end) {
        super(opType.getTokenType(), start, end);
        this.opType = opType;
    }

    public JSOperatorType getOpType() {
        return opType;
    }

}
