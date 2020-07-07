package p0nki.pesl.api.builtins;

import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.*;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PESLDataUtils {

    private PESLDataUtils() {

    }

    private static final byte ARRAY = 0;
    private static final byte BOOLEAN = 1;
    private static final byte FUNCTION = 2;
    private static final byte MAP = 3;
    private static final byte NUMBER = 4;
    private static final byte STRING = 5;
    private static final byte UNDEFINED = 6;
    private static final byte NULL = 7;

    public static void write(@Nonnull PESLObject object, @Nonnull DataOutputStream outputStream) throws IOException, PESLEvalException {
        if (object instanceof ArrayObject) {
            outputStream.writeByte(ARRAY);
            outputStream.writeInt(((ArrayObject) object).getValues().size());
            for (PESLObject value : ((ArrayObject) object).getValues()) {
                write(value, outputStream);
            }
        } else if (object instanceof BooleanObject) {
            outputStream.writeByte(BOOLEAN);
            outputStream.writeBoolean(((BooleanObject) object).getValue());
        } else if (object instanceof FunctionObject) {
            throw new PESLEvalException("Cannot write function");
        } else if (object instanceof MapObject) {
            outputStream.writeByte(MAP);
            outputStream.writeInt(((MapObject) object).getValues().size());
            for (Map.Entry<String, PESLObject> value : ((MapObject) object).getValues().entrySet()) {
                outputStream.writeInt(value.getKey().length());
                for (char c : value.getKey().toCharArray()) {
                    outputStream.writeChar(c);
                }
                write(value.getValue(), outputStream);
            }
        } else if (object instanceof NumberObject) {
            outputStream.writeByte(NUMBER);
            outputStream.writeDouble(((NumberObject) object).getValue());
        } else if (object instanceof StringObject) {
            outputStream.writeByte(STRING);
            outputStream.writeInt(((StringObject) object).getValue().length());
            for (char c : ((StringObject) object).getValue().toCharArray()) {
                outputStream.writeChar(c);
            }
        } else if (object instanceof UndefinedObject) {
            outputStream.writeByte(UNDEFINED);
        } else if (object instanceof NullObject) {
            outputStream.writeByte(NULL);
        } else {
            throw new UnsupportedOperationException("Cannot write " + object.getType());
        }
    }

    @Nonnull
    public static PESLObject read(@Nonnull DataInputStream inputStream) throws IOException, PESLEvalException {
        byte type = inputStream.readByte();
        if (type == ARRAY) {
            int length = inputStream.readInt();
            List<PESLObject> values = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                values.add(read(inputStream));
            }
            return new ArrayObject(values);
        } else if (type == BOOLEAN) {
            return new BooleanObject(inputStream.readBoolean());
        } else if (type == FUNCTION) {
            throw new PESLEvalException("Cannot read object of type FUNCTION");
        } else if (type == MAP) {
            int size = inputStream.readInt();
            Map<String, PESLObject> values = new HashMap<>();
            for (int i = 0; i < size; i++) {
                int length = inputStream.readInt();
                StringBuilder key = new StringBuilder();
                for (int j = 0; j < length; j++) {
                    key.append(inputStream.readChar());
                }
                values.put(key.toString(), read(inputStream));
            }
            return new MapObject(values);
        } else if (type == NULL) {
            return NullObject.INSTANCE;
        } else if (type == NUMBER) {
            return new NumberObject(inputStream.readDouble());
        } else if (type == STRING) {
            int length = inputStream.readInt();
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < length; i++) {
                value.append(inputStream.readChar());
            }
            return new StringObject(value.toString());
        } else if (type == UNDEFINED) {
            return UndefinedObject.INSTANCE;
        } else {
            throw new UnsupportedOperationException("Cannot read object of type " + type);
        }
    }

    public static PESLObject copy(@Nonnull PESLObject object) throws PESLEvalException {
        if (object instanceof ArrayObject) {
            List<PESLObject> values = new ArrayList<>();
            for (int i = 0; i < ((ArrayObject) object).arraySize(); i++) {
                values.add(copy(((ArrayObject) object).getElement(i)));
            }
            return new ArrayObject(values);
        } else if (object instanceof BooleanObject) {
            return new BooleanObject(((BooleanObject) object).getValue());
        } else if (object instanceof FunctionObject) {
            throw new PESLEvalException("Cannot copy function");
        } else if (object instanceof MapObject) {
            Map<String, PESLObject> values = new HashMap<>();
            for (String key : ((MapObject) object).keys()) {
                values.put(key, copy(((MapObject) object).getKey(key)));
            }
            return new MapObject(values);
        } else if (object instanceof NullObject) {
            return NullObject.INSTANCE;
        } else if (object instanceof NumberObject) {
            return new NumberObject(((NumberObject) object).getValue());
        } else if (object instanceof StringObject) {
            return new StringObject(((StringObject) object).getValue());
        } else if (object instanceof UndefinedObject) {
            return UndefinedObject.INSTANCE;
        } else {
            throw new UnsupportedOperationException("Cannot copy " + object.getType());
        }
    }

    public static boolean deepEqualsArray(@Nonnull ArrayLikeObject a, @Nonnull ArrayLikeObject b) {
        if (a.arraySize() != b.arraySize()) return false;
        for (int i = 0; i < a.arraySize(); i++) {
            if (!deepEquals(a.getElementNoChecks(i), b.getElementNoChecks(i))) return false;
        }
        return true;
    }

    public static boolean deepEqualsMapLike(@Nonnull MapObject a, @Nonnull MapObject b) {
        if (a.keys().size() != b.keys().size()) return false;
        for (String key : a.keys()) {
            if (!b.keys().contains(key)) return false;
            if (!deepEquals(a.getKey(key), b.getKey(key))) return false;
        }
        return true;
    }

    public static boolean deepEquals(@Nonnull PESLObject a, @Nonnull PESLObject b) {
        if (!a.getType().equals(b.getType())) return false;
        if (a instanceof FunctionObject || b instanceof FunctionObject) return false;
        if (a instanceof ArrayLikeObject && b instanceof ArrayLikeObject) {
            return deepEqualsArray((ArrayLikeObject) a, (ArrayLikeObject) b);
        } else if (a instanceof BooleanObject && b instanceof BooleanObject) {
            return ((BooleanObject) a).getValue() == ((BooleanObject) b).getValue();
        } else if (a instanceof StringObject && b instanceof StringObject) {
            return ((StringObject) a).getValue().equals(((StringObject) b).getValue());
        } else if (a instanceof MapObject && b instanceof MapObject) {
            return deepEqualsMapLike((MapObject) a, (MapObject) b);
        } else if (a instanceof NullObject && b instanceof NullObject) {
            if (a != b) throw new AssertionError("Expected all instances of `null` to represent the same object");
            return true;
        } else if (a instanceof NumberObject && b instanceof NumberObject) {
            return ((NumberObject) a).getValue() == ((NumberObject) b).getValue();
        } else if (a instanceof UndefinedObject && b instanceof UndefinedObject) {
            if (a != b) throw new AssertionError("Expected all instances of `undefined` to represent the same object");
            return true;
        } else {
            System.err.println("[WARNING] cannot compare types " + a.getType() + " and " + b.getType());
            return false;
        }
    }

}
