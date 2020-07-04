package p0nki.pesl.api.object;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class NullObject extends PESLObject {

    public static final NullObject INSTANCE = new NullObject();

    private NullObject() {
        super("null");
    }


    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "null";
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return "null";
    }
}
