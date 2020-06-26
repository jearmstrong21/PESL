package p0nki.pesl.api.parse;

import p0nki.pesl.internal.token.type.PESLToken;
import p0nki.pesl.internal.token.type.TokenType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class PESLParseException extends Exception {

    private final PESLToken token;

    public PESLParseException(String message, @Nonnull PESLToken token) {
        super(message);
        this.token = Objects.requireNonNull(token);
    }

    public PESLParseException(@Nonnull PESLToken got, @Nonnull TokenType... expected) {
        super("Expected " + Arrays.stream(expected).map(TokenType::toString).collect(Collectors.joining(", ")) + ", got " + got.toString());
        this.token = got;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " at token " + token.toString();
    }

    @Nonnull
    public PESLToken getToken() {
        return token;
    }
}
