package p0nki.javashit.ast;

import p0nki.javashit.token.type.JSToken;
import p0nki.javashit.token.type.JSTokenType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JSParseException extends Exception {

    public static final JSParseException UNEXPECTED_EOF = new JSParseException("Unexpected EOF");

    public JSParseException(String message) {
        super(message);
    }

    public JSParseException(JSToken got, JSTokenType... expected) {
        super("Expected " + Arrays.stream(expected).map(JSTokenType::toString).collect(Collectors.joining(", ")) + ", got " + got.toString());
    }

}
