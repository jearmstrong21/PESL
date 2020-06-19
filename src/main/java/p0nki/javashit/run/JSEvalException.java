package p0nki.javashit.run;

public class JSEvalException extends Exception {

    public JSEvalException(String message) {
        super(message);
    }

    public static JSEvalException arrayIndexOutOfBounds(int index, int size) {
        return new JSEvalException(String.format("Array index out of bounds: %s, for array of size %s", index, size));
    }

    public static JSEvalException expectedNumber(String value) {
        return new JSEvalException(String.format("Expected number, got %s", value));
    }

}
