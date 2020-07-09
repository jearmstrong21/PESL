package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class UndefinedObject extends PESLObject {

    public static final String TYPE = "undefined";

    public static final UndefinedObject INSTANCE = new UndefinedObject();

    private UndefinedObject() {
        super(TYPE);
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "undefined";
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return "undefined";
    }

    @Override
    public boolean compareEquals(@Nonnull PESLObject object) {
        if (this != INSTANCE)
            throw new UnsupportedOperationException("Unsupported non-INSTANCE UNDEFINED object found");
        return object == INSTANCE;
    }
}
