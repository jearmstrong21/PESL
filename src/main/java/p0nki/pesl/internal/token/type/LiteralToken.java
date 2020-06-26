package p0nki.pesl.internal.token.type;

public class LiteralToken extends PESLToken {

    private final String value;

    public LiteralToken(String value, int start, int end) {
        super(TokenType.LITERAL, start, end);
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
