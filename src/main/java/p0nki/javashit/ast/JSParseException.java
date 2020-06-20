package p0nki.javashit.ast;

import p0nki.javashit.token.type.JSToken;
import p0nki.javashit.token.type.JSTokenType;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class JSParseException extends Exception {

    private final JSToken token;

    public JSParseException(String message, JSToken token) {
        super(message);
        this.token = Objects.requireNonNull(token);
    }

    public JSParseException(JSToken got, JSTokenType... expected) {
        super("Expected " + Arrays.stream(expected).map(JSTokenType::toString).collect(Collectors.joining(", ")) + ", got " + got.toString());
        this.token = got;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " at token " + token.toString();
    }

    public JSToken getToken() {
        return token;
    }
}
