package p0nki.javashit.builtins;

import p0nki.javashit.object.*;
import p0nki.javashit.run.JSEvalException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSSerializationUtils {

    private JSSerializationUtils() {

    }

    public static void writeObject(JSObject object, DataOutputStream outputStream) throws IOException, JSEvalException {
        outputStream.writeInt(object.type().ordinal());
        if (object instanceof JSArray) {
            outputStream.writeInt(((JSArray) object).getValues().size());
            for (JSObject value : ((JSArray) object).getValues()) {
                writeObject(value, outputStream);
            }
        } else if (object instanceof JSBooleanObject) {
            outputStream.writeBoolean(((JSBooleanObject) object).getValue());
        } else if (object instanceof JSFunction) {
            throw new JSEvalException("Cannot write FUNCTION");
        } else if (object instanceof JSMap) {
            outputStream.writeInt(((JSMap) object).getValues().size());
            for (Map.Entry<String, JSObject> value : ((JSMap) object).getValues().entrySet()) {
                outputStream.writeInt(value.getKey().length());
                for (char c : value.getKey().toCharArray()) {
                    outputStream.writeChar(c);
                }
                writeObject(value.getValue(), outputStream);
            }
        } else if (object instanceof JSNullObject) {

        } else if (object instanceof JSNumberObject) {
            outputStream.writeDouble(((JSNumberObject) object).getValue());
        } else if (object instanceof JSStringObject) {
            outputStream.writeInt(((JSStringObject) object).getValue().length());
            for (char c : ((JSStringObject) object).getValue().toCharArray()) {
                outputStream.writeChar(c);
            }
        } else if (object instanceof JSUndefinedObject) {

        } else {
            throw new UnsupportedOperationException("Cannot write object of type " + object.type());
        }
    }

    public static JSObject readObject(DataInputStream inputStream) throws IOException, JSEvalException {
        int type = inputStream.readInt();
        if (type == JSObjectType.ARRAY.ordinal()) {
            int length = inputStream.readInt();
            List<JSObject> values = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                values.add(readObject(inputStream));
            }
            return new JSArray(values);
        } else if (type == JSObjectType.BOOLEAN.ordinal()) {
            return new JSBooleanObject(inputStream.readBoolean());
        } else if (type == JSObjectType.FUNCTION.ordinal()) {
            throw new JSEvalException("Cannot read object of type FUNCTION");
        } else if (type == JSObjectType.MAP.ordinal()) {
            int size = inputStream.readInt();
            Map<String, JSObject> values = new HashMap<>();
            for (int i = 0; i < size; i++) {
                int length = inputStream.readInt();
                StringBuilder key = new StringBuilder();
                for (int j = 0; j < length; j++) {
                    key.append(inputStream.readChar());
                }
                values.put(key.toString(), readObject(inputStream));
            }
            return new JSMap(values);
        } else if (type == JSObjectType.NULL.ordinal()) {
            return JSNullObject.INSTANCE;
        } else if (type == JSObjectType.NUMBER.ordinal()) {
            return new JSNumberObject(inputStream.readDouble());
        } else if (type == JSObjectType.STRING.ordinal()) {
            int length = inputStream.readInt();
            String value = "";
            for (int i = 0; i < length; i++) {
                value += inputStream.readChar();
            }
            return new JSStringObject(value);
        } else if (type == JSObjectType.UNDEFINED.ordinal()) {
            return JSUndefinedObject.INSTANCE;
        } else {
            throw new UnsupportedOperationException("Cannot read object of type " + type);
        }
    }

}
