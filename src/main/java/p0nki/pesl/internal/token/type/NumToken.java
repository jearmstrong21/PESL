package p0nki.pesl.internal.token.type;

public class NumToken extends PESLToken {

    private final double value;
    private final boolean hasFP;

    public NumToken(double value, int start, int end, boolean hasFP) {
        super(TokenType.NUMBER, start, end);
        this.value = value;
        this.hasFP = hasFP;
    }

    public boolean isHasFP() {
        return hasFP;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NUM[" + value + "]";
    }
}
