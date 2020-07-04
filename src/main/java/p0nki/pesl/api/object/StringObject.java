package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StringObject extends PESLObject implements MapLikeObject {

    public static final Set<String> KEYS = Collections.unmodifiableSet(new HashSet<String>() {{
        add("charAt");
        add("substring");
        add("length");
    }});
    private final String value;
    private final FunctionObject charAt;
    private final FunctionObject substring;
    private final FunctionObject length;


    public StringObject(String value) {
        super("string");
        this.value = value;
        charAt = FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 1);
            return new StringObject("" + value.charAt((int) arguments.get(0).asNumber().getValue()));
        });
        substring = FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 1, 2);
            int beginIndex = (int) arguments.get(0).asNumber().getValue();
            PESLEvalException.checkIndexOutOfBounds(beginIndex, value.length());
            if (arguments.size() == 1) return new StringObject(value.substring(beginIndex));
            int endIndex = (int) arguments.get(1).asNumber().getValue();
            PESLEvalException.checkIndexOutOfBounds(endIndex, value.length());
            if (beginIndex > endIndex) throw PESLEvalException.indexOutOfBounds(beginIndex, value.length());
            return new StringObject(value.substring(beginIndex, endIndex));
        });
        length = FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 0);
            return new NumberObject(value.length());
        });
    }

    public String getValue() {
        return value;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "\"" + value + "\"";
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return value;
    }

    @Nonnull
    @Override
    public PESLObject get(String key) {
        if (key.equals("charAt")) return charAt;
        if (key.equals("substring")) return substring;
        if (key.equals("length")) return length;
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void set(@Nonnull String key, @Nonnull PESLObject value) throws PESLEvalException {
        throw PESLEvalException.cannotSetKey(key);
    }

    @Override
    public @Nonnull
    Set<String> keys() {
        return KEYS;
    }
}
