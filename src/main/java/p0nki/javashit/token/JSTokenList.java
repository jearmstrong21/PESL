package p0nki.javashit.token;

import p0nki.javashit.ast.JSParseException;
import p0nki.javashit.token.type.JSToken;
import p0nki.javashit.token.type.JSTokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JSTokenList {

    private final List<JSToken> tokens;
    private JSToken lastPoppedToken = null;

    public JSTokenList() {
        tokens = new ArrayList<>();
    }

    public int getSize() {
        return tokens.size();
    }

    public JSToken get(int index) {
        return tokens.get(index);
    }

    public JSTokenList(List<JSToken> tokens) {
        this.tokens = tokens;
    }

    public void push(JSToken token) {
        tokens.add(token);
    }

    public void push(JSTokenType type, int start, int end) {
        tokens.add(new JSToken(type, start, end));
    }

    public boolean hasAny() {
        return tokens.size() > 0;
    }

    @SuppressWarnings("unchecked")
    public <T extends JSToken> T peek() throws JSParseException {
        if (tokens.size() == 0) throw new JSParseException("Unexpected EOF", Objects.requireNonNull(lastPoppedToken));
        return (T) tokens.get(0);
    }

    public JSToken pop() throws JSParseException {
        if (tokens.size() == 0) {
            throw new JSParseException("Unexpected EOF", Objects.requireNonNull(lastPoppedToken));
        }
        lastPoppedToken = tokens.remove(0);
        return lastPoppedToken;
    }

    @SuppressWarnings("unchecked")
    public <T extends JSToken> T expect(JSTokenType... types) throws JSParseException {
        JSToken token = pop();
        for (JSTokenType type : types) {
            if (token.getType() == type) return (T) token;
        }
        throw new JSParseException(token, types);
    }

}
