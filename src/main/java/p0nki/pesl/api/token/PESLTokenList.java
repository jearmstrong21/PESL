package p0nki.pesl.api.token;

import p0nki.pesl.api.parse.PESLParseException;
import p0nki.pesl.internal.token.type.LiteralToken;
import p0nki.pesl.internal.token.type.PESLToken;
import p0nki.pesl.internal.token.type.TokenType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PESLTokenList {

    private final List<PESLToken> tokens;
    private PESLToken lastPoppedToken = null;

    public PESLTokenList() {
        tokens = new ArrayList<>();
    }

    public PESLTokenList(List<PESLToken> tokens) {
        this.tokens = tokens;
    }

    public int getSize() {
        return tokens.size();
    }

    @Nonnull
    public PESLToken get(int index) {
        return tokens.get(index);
    }

    public void push(@Nonnull PESLToken token) {
        tokens.add(token);
    }

    public void push(@Nonnull TokenType type, int start, int end) {
        tokens.add(new PESLToken(type, start, end));
    }

    public boolean hasAny() {
        return tokens.size() > 0;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public <T extends PESLToken> T peek() throws PESLParseException {
        if (tokens.size() == 0) throw new PESLParseException("Unexpected EOF", Objects.requireNonNull(lastPoppedToken));
        return (T) tokens.get(0);
    }

    @Nonnull
    public PESLToken pop() throws PESLParseException {
        if (tokens.size() == 0) {
            throw new PESLParseException("Unexpected EOF", Objects.requireNonNull(lastPoppedToken));
        }
        lastPoppedToken = tokens.remove(0);
        return lastPoppedToken;
    }

    public String literal() throws PESLParseException {
        LiteralToken token = expect(TokenType.LITERAL);
        return token.getValue();
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public <T extends PESLToken> T expect(TokenType... types) throws PESLParseException {
        PESLToken token = pop();
        for (TokenType type : types) {
            if (token.getType() == type) return (T) token;
        }
        throw new PESLParseException(token, types);
    }

}
