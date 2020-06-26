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

    public static void writeObject(@Nonnull PESLObject object, @Nonnull DataOutputStream outputStream) throws IOException, PESLEvalException {
        outputStream.writeInt(object.type().ordinal());
        if (object instanceof ArrayObject) {
            outputStream.writeInt(((ArrayObject) object).getValues().size());
            for (PESLObject value : ((ArrayObject) object).getValues()) {
                writeObject(value, outputStream);
            }
        } else if (object instanceof BooleanObject) {
            outputStream.writeBoolean(((BooleanObject) object).getValue());
        } else if (object instanceof FunctionObject) {
            throw new PESLEvalException("Cannot write FUNCTION");
        } else if (object instanceof MapObject) {
            outputStream.writeInt(((MapObject) object).getValues().size());
            for (Map.Entry<String, PESLObject> value : ((MapObject) object).getValues().entrySet()) {
                outputStream.writeInt(value.getKey().length());
                for (char c : value.getKey().toCharArray()) {
                    outputStream.writeChar(c);
                }
                writeObject(value.getValue(), outputStream);
            }
        } else if (object instanceof NumberObject) {
            outputStream.writeDouble(((NumberObject) object).getValue());
        } else if (object instanceof StringObject) {
            outputStream.writeInt(((StringObject) object).getValue().length());
            for (char c : ((StringObject) object).getValue().toCharArray()) {
                outputStream.writeChar(c);
            }
        } else if (!(object instanceof UndefinedObject) && !(object instanceof NullObject)) {
            throw new UnsupportedOperationException("Cannot write object of type " + object.type());
        }
    }

    @Nonnull
    public static PESLObject readObject(@Nonnull DataInputStream inputStream) throws IOException, PESLEvalException {
        int type = inputStream.readInt();
        if (type == ObjectType.ARRAY.ordinal()) {
            int length = inputStream.readInt();
            List<PESLObject> values = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                values.add(readObject(inputStream));
            }
            return new ArrayObject(values);
        } else if (type == ObjectType.BOOLEAN.ordinal()) {
            return new BooleanObject(inputStream.readBoolean());
        } else if (type == ObjectType.FUNCTION.ordinal()) {
            throw new PESLEvalException("Cannot read object of type FUNCTION");
        } else if (type == ObjectType.MAP.ordinal()) {
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
        } else if (type == ObjectType.NULL.ordinal()) {
            return NullObject.INSTANCE;
        } else if (type == ObjectType.NUMBER.ordinal()) {
            return new NumberObject(inputStream.readDouble());
        } else if (type == ObjectType.STRING.ordinal()) {
            int length = inputStream.readInt();
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < length; i++) {
                value.append(inputStream.readChar());
            }
            return new StringObject(value.toString());
        } else if (type == ObjectType.UNDEFINED.ordinal()) {
            return UndefinedObject.INSTANCE;
        } else {
            throw new UnsupportedOperationException("Cannot read object of type " + type);
        }
    }

}
