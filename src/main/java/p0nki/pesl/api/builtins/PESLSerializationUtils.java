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

public class PESLSerializationUtils {

    private PESLSerializationUtils() {

    }

    private static final byte ARRAY = 0;
    private static final byte BOOLEAN = 1;
    private static final byte FUNCTION = 2;
    private static final byte MAP = 3;
    private static final byte NUMBER = 4;
    private static final byte STRING = 5;
    private static final byte UNDEFINED = 6;
    private static final byte NULL = 7;

    public static void writeObject(@Nonnull PESLObject object, @Nonnull DataOutputStream outputStream) throws IOException, PESLEvalException {
        if (object instanceof ArrayObject) {
            outputStream.writeByte(ARRAY);
            outputStream.writeInt(((ArrayObject) object).getValues().size());
            for (PESLObject value : ((ArrayObject) object).getValues()) {
                writeObject(value, outputStream);
            }
        } else if (object instanceof BooleanObject) {
            outputStream.writeByte(BOOLEAN);
            outputStream.writeBoolean(((BooleanObject) object).getValue());
        } else if (object instanceof FunctionObject) {
//            outputStream.writeByte(FUNCTION);
            throw new PESLEvalException("Cannot write function");
        } else if (object instanceof MapObject) {
            outputStream.writeByte(MAP);
            outputStream.writeInt(((MapObject) object).getValues().size());
            for (Map.Entry<String, PESLObject> value : ((MapObject) object).getValues().entrySet()) {
                outputStream.writeInt(value.getKey().length());
                for (char c : value.getKey().toCharArray()) {
                    outputStream.writeChar(c);
                }
                writeObject(value.getValue(), outputStream);
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
    public static PESLObject readObject(@Nonnull DataInputStream inputStream) throws IOException, PESLEvalException {
        byte type = inputStream.readByte();
        if (type == ARRAY) {
            int length = inputStream.readInt();
            List<PESLObject> values = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                values.add(readObject(inputStream));
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
                values.put(key.toString(), readObject(inputStream));
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

}
