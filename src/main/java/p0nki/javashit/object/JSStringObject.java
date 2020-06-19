package p0nki.javashit.object;

public class JSStringObject extends JSObject {

    private final String value;

    public JSStringObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String stringify() {
        return "\"" + value + "\"";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.STRING;
    }
}
