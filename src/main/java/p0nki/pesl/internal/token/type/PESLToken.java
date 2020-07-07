package p0nki.pesl.internal.token.type;

public class PESLToken {

    private final TokenType type;
    private final int start, end;

    public PESLToken(TokenType type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
        if (type == TokenType.NUMBER && !(this instanceof NumToken))
            throw new UnsupportedOperationException("Cannot initialize generic PESLToken with type NUMBER");
        if ((type == TokenType.ADDITIVE_OP ||
                type == TokenType.MULTIPLICATIVE_OP ||
                type == TokenType.BOOLEAN_OP ||
                type == TokenType.COMPARATIVE_OP) && !(this instanceof OperatorToken))
            throw new UnsupportedOperationException("Cannot initialize generic PESLToken with type " + type.toString());
        if (type == TokenType.LITERAL && !(this instanceof LiteralToken))
            throw new UnsupportedOperationException("Cannot initialize generic PESLToken with type UNQUOTED_LITERAL");
        if (type == TokenType.ASSIGNMENT_OP && !(this instanceof AssignmentOpToken))
            throw new UnsupportedOperationException("Cannot iniitalize generic PESLToken with type ASSIGNMENT_OP");
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
