package p0nki.pesl.api;

import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.StringObject;

import java.util.List;

public class PESLEvalException extends Exception {

    public static final PESLEvalException INVALID_ARGUMENT_LIST = new PESLEvalException("Invalid argument list");

    private final PESLObject object;

    public PESLEvalException(String message) {
        this(new StringObject(message));
    }

    public PESLEvalException(PESLObject object) {
        super(object.stringify());
        this.object = object;
    }

    public static PESLEvalException indexOutOfBounds(int index, int size) {
        return new PESLEvalException(String.format("Index out of bounds: %s, for array of length %s", index, size));
    }

    public static int checkIndexOutOfBounds(int index, int size) throws PESLEvalException {
        if (index < 0 || index >= size) throw indexOutOfBounds(index, size);
        return index;
    }

    public static PESLEvalException cannotSetKey(String value) {
        return new PESLEvalException("Cannot set key " + value);
    }

    public static void validArgumentListLength(List<PESLObject> arguments, int... lengths) throws PESLEvalException {
        boolean bad = true;
        for (int length : lengths) {
            if (arguments.size() == length) {
                bad = false;
                break;
            }
        }
        if (bad)
            throw INVALID_ARGUMENT_LIST;
    }

    public PESLObject getObject() {
        return object;
    }

}
