package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class UndefinedObject extends PESLObject {

    public static final UndefinedObject INSTANCE = new UndefinedObject();

    private UndefinedObject() {
        super("undefined");
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
}
