package p0nki.javashit.token;

public class JSTokenizeException extends Exception {

    public static final JSTokenizeException UNEXPECTED_EOF = new JSTokenizeException("Unexpected EOF while parsing");

    public static JSTokenizeException INVALID_BUFFER(String message) {
        return new JSTokenizeException("Invalid buffer `" + message + "`");
    }

    private JSTokenizeException(String message) {
        super(message);
    }

//    public JSTokenException(JSToken got, JSTokenType... expected) {
//        super("Expected " + Arrays.stream(expected).map(JSTokenType::toString).collect(Collectors.joining(", ")) + ", got " + got.toString());
//    }

}
