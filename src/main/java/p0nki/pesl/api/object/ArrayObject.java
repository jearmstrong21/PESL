package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayObject extends BuiltinMapLikeObject implements ArrayLikeObject {

    public static final String TYPE = "array";

    private final List<PESLObject> values;

    public ArrayObject(@Nonnull List<PESLObject> values) {
        super(TYPE);
        this.values = values;
        put("pop", FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 0);
            if (values.size() == 0) throw PESLEvalException.indexOutOfBounds(-1, 0);
            values.remove(values.size() - 1);
            return UndefinedObject.INSTANCE;
        }));
        put("add", FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 1, 2);
            if (arguments.size() == 2) {
                values.add(PESLEvalException.checkIndexOutOfBounds((int) arguments.get(0).asNumber().getValue(), values.size() + 1), arguments.get(1));
            } else {
                values.add(arguments.get(0));
            }
            return UndefinedObject.INSTANCE;
        }));
        put("length", FunctionObject.of(false, arguments -> {
            PESLEvalException.validArgumentListLength(arguments, 0);
            return new NumberObject(values.size());
        }));
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
    public boolean compareEquals(@Nonnull PESLObject object) {
        if (object instanceof ArrayObject) {
            return isEqual(this, (MapLikeObject) object);
        }
        return false;
    }

    @Nonnull
    @Override
    public PESLObject getElement(int index) throws PESLEvalException {
        return values.get(PESLEvalException.checkIndexOutOfBounds(index, values.size()));
    }

    @Override
    public void setElement(int index, @Nonnull PESLObject object) throws PESLEvalException {
        if (index < 0) PESLEvalException.checkIndexOutOfBounds(index, values.size());
        if (index >= values.size()) {
            for (int i = 0; i < index - values.size(); i++) {
                values.add(UndefinedObject.INSTANCE);
            }
            values.add(object);
        } else {
            values.set(index, object);
        }
    }

    @Nonnull
    @Override
    public PESLObject removeElement(int index) throws PESLEvalException {
        return values.remove(PESLEvalException.checkIndexOutOfBounds(index, values.size()));
    }

    @Override
    public int arraySize() {
        return values.size();
    }
}
