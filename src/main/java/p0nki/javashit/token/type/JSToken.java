package p0nki.javashit.token.type;

public class JSToken {

    private final JSTokenType type;

    public JSToken(JSTokenType type) {
        this.type = type;
        if (type == JSTokenType.NUMBER && !(this instanceof JSNumToken))
            throw new UnsupportedOperationException("Cannot initialize generic JSToken with type NUMBER");
        if ((type == JSTokenType.ADDITIVE_OP || type == JSTokenType.MULTIPLICATIVE_OP) && !(this instanceof JSOperatorToken))
            throw new UnsupportedOperationException("Cannot initialize generic JSToken with type " + type.toString());
        if (type == JSTokenType.LITERAL && !(this instanceof JSLiteralToken))
            throw new UnsupportedOperationException("Cannot initialize generic JSToken with type UNQUOTED_LITERAL");
    }

    public JSTokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
