package p0nki.javashit.token.type;

public class JSNumToken extends JSToken {

    private final double value;

    public JSNumToken(double value, int start, int end) {
        super(JSTokenType.NUMBER, start, end);
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
