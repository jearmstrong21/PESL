package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayObject extends PESLObject implements MapLikeObject {

    private final List<PESLObject> values;
    private final FunctionObject push;
    private final FunctionObject pop;
    private final FunctionObject length;

    public ArrayObject(@Nonnull List<PESLObject> values) {
        super("array");
        this.values = values;
        push = FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 1);
            values.add(arguments.get(0));
            return UndefinedObject.INSTANCE;
        });
        pop = FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 0);
            if (values.size() == 0) throw PESLEvalException.indexOutOfBounds(-1, 0);
            values.remove(values.size() - 1);
            return UndefinedObject.INSTANCE;
        });
        length = FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 0);
            return new NumberObject(values.size());
        });
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return "[" + values.stream().map(PESLObject::stringify).collect(Collectors.joining(", ")) + "]";
    }

    @Nonnull
    public List<PESLObject> getValues() {
        return values;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return stringify();
    }

    @Override
    public @Nonnull
    PESLObject get(String key) throws PESLEvalException {
        if (key.equals("push")) return push;
        if (key.equals("pop")) return pop;
        if (key.equals("length")) return length;
        try {
            return values.get(PESLEvalException.checkIndexOutOfBounds((int) Double.parseDouble(key), values.size()));
        } catch (NumberFormatException nfe) {
            return UndefinedObject.INSTANCE;
        }
    }

    @Override
    public void set(@Nonnull String key, @Nonnull PESLObject value) throws PESLEvalException {
        try {
            values.set(PESLEvalException.checkIndexOutOfBounds((int) Double.parseDouble(key), values.size()), value);
        } catch (NumberFormatException nfe) {
            throw PESLEvalException.cannotSetKey(key);
        }
    }

    @Override
    public @Nonnull
    Set<String> keys() {
        Set<String> keys = IntStream.range(0, values.size()).boxed().map(x -> x + "").collect(Collectors.toSet());
        keys.add("push");
        keys.add("pop");
        keys.add("length");
        return Collections.unmodifiableSet(keys);
    }
}
