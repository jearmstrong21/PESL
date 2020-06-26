package p0nki.pesl.internal.token.type;

public class NumToken extends PESLToken {

    private final double value;

    public NumToken(double value, int start, int end) {
        super(TokenType.NUMBER, start, end);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NUM[" + value + "]";
    }
}
