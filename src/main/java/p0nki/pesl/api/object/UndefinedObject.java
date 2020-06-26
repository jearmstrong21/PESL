package p0nki.pesl.api.object;

import javax.annotation.Nonnull;

public class UndefinedObject extends PESLObject {

    public static final UndefinedObject INSTANCE = new UndefinedObject();

    private UndefinedObject() {

    }

    @Nonnull
    @Override
    public String stringify() {
        return "undefined";
    }

    @Nonnull
    @Override
    public ObjectType type() {
        return ObjectType.UNDEFINED;
    }

    @Nonnull
    @Override
    public String castToString() {
        return "undefined";
    }
}
