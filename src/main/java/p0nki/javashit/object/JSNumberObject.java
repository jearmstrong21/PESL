package p0nki.javashit.object;

public class JSNumberObject extends JSObject {

    private final double value;

    public JSNumberObject(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String stringify() {
        return value + "";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.NUMBER;
    }

    @Override
    public String castToString() {
        return value+"";
    }

}
