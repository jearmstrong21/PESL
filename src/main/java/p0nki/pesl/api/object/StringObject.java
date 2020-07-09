package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StringObject extends BuiltinMapLikeObject {

    public static final String TYPE = "string";

    public static final Set<String> KEYS = Collections.unmodifiableSet(new HashSet<String>() {{
        add("charAt");
        add("substring");
        add("length");
    }});
    private final String value;

    public StringObject(String value) {
        super(TYPE);
        this.value = value;
        put("charAt", FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 1);
            return new StringObject("" + value.charAt((int) arguments.get(0).asNumber().getValue()));
        }));
        put("substring", FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 1, 2);
            int beginIndex = (int) arguments.get(0).asNumber().getValue();
            PESLEvalException.checkIndexOutOfBounds(beginIndex, value.length());
            if (arguments.size() == 1) return new StringObject(value.substring(beginIndex));
            int endIndex = (int) arguments.get(1).asNumber().getValue();
            PESLEvalException.checkIndexOutOfBounds(endIndex, value.length());
            if (beginIndex > endIndex) throw PESLEvalException.indexOutOfBounds(beginIndex, value.length());
            return new StringObject(value.substring(beginIndex, endIndex));
        }));
        put("length", FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 0);
            return new NumberObject(value.length());
        }));
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

    @Override
    public boolean compareEquals(@Nonnull PESLObject object) {
        return object instanceof StringObject && value.equals(((StringObject) object).getValue());
    }

}
