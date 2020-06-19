package p0nki.javashit.token.type;

public class JSNumToken extends JSToken {

    private final double value;

    public JSNumToken(double value) {
        super(JSTokenType.NUMBER);
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
