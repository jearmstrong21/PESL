package p0nki.pesl.api.object;

import javax.annotation.Nonnull;

public class NullObject extends PESLObject {

    public static final NullObject INSTANCE = new NullObject();

    private NullObject() {

    }


    @Nonnull
    @Override
    public String stringify() {
        return "null";
    }

    @Nonnull
    @Override
    public ObjectType type() {
        return ObjectType.NULL;
    }

    @Nonnull
    @Override
    public String castToString() {
        return "null";
    }
}
