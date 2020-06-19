package p0nki.javashit.token.type;

public class JSOperatorToken extends JSToken {

    private final JSOperatorType opType;

    public JSOperatorToken(JSOperatorType opType) {
        super(opType.getTokenType());
        this.opType = opType;
    }

    public JSOperatorType getOpType() {
        return opType;
    }

}
