package p0nki.javashit.object;

public class JSNullObject extends JSObject {

    public static final JSNullObject INSTANCE = new JSNullObject();

    private JSNullObject() {

    }


    @Override
    public String stringify() {
        return "null";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.NULL;
    }

    @Override
    public String castToString() {
        return "null";
    }
}
