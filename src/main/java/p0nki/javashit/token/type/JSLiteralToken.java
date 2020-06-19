package p0nki.javashit.token.type;

public class JSLiteralToken extends JSToken {

    private final String value;

    public JSLiteralToken(String value) {
        super(JSTokenType.LITERAL);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LITERAL[" + value + "]";
    }
}
