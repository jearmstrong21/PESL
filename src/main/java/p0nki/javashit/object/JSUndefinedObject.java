package p0nki.javashit.object;

public class JSUndefinedObject extends JSObject {

    public static final JSUndefinedObject INSTANCE = new JSUndefinedObject();

    private JSUndefinedObject() {

    }

    @Override
    public String stringify() {
        return "undefined";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.UNDEFINED;
    }

    @Override
    public String castToString() {
        return "undefined";
    }
}
