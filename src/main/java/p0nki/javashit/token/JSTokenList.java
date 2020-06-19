package p0nki.javashit.token;

import p0nki.javashit.ast.JSParseException;
import p0nki.javashit.token.type.JSToken;
import p0nki.javashit.token.type.JSTokenType;

import java.util.ArrayList;
import java.util.List;

public class JSTokenList {

    private final List<JSToken> tokens;

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

    public void push(JSTokenType type) {
        tokens.add(new JSToken(type));
    }

    public boolean hasAny() {
        return tokens.size() > 0;
    }

    public <T extends JSToken> T  peek() throws JSParseException {
        if (tokens.size() == 0) throw JSParseException.UNEXPECTED_EOF;
        return (T) tokens.get(0);
    }

    public JSToken pop() throws JSParseException {
        if (tokens.size() == 0) {
            throw JSParseException.UNEXPECTED_EOF;
        }
        return tokens.remove(0);
    }

    public <T extends JSToken> T expect(JSTokenType... types) throws JSParseException {
        JSToken token = pop();
        for (JSTokenType type : types) {
            if (token.getType() == type) return (T) token;
        }
        throw new JSParseException(token, types);
    }

}
