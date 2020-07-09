package p0nki.pesl.api.object;

public class ObjectType {

    public static final ObjectType BOOLEAN = new ObjectType(false, false, BooleanObject.TYPE);
    public static final ObjectType NUMBER = new ObjectType(false, false, NumberObject.TYPE);
    public static final ObjectType MAP = new ObjectType(false, true, MapObject.TYPE);
    public static final ObjectType ARRAY = new ObjectType(true, true, ArrayObject.TYPE);
    public static final ObjectType UNDEFINED = new ObjectType(false, false, UndefinedObject.TYPE);
    public static final ObjectType FUNCTION = new ObjectType(false, false, FunctionObject.TYPE);
    private final boolean arrayLike;
    private final boolean mapLike;
    private final String declaredType;

    private ObjectType(boolean arrayLike, boolean mapLike, String declaredType) {
        this.arrayLike = arrayLike;
        this.mapLike = mapLike;
        this.declaredType = declaredType;
    }

    public ObjectType(PESLObject object) {
        this(object instanceof ArrayLikeObject, object instanceof MapLikeObject, object.getType());
    }

    public boolean isArrayLike() {
        return arrayLike;
    }

    public boolean isMapLike() {
        return mapLike;
    }

    public String getDeclaredType() {
        return declaredType;
    }
}
