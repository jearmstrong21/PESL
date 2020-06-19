package p0nki.javashit.token;

import p0nki.javashit.Utilities;
import p0nki.javashit.token.type.*;

import java.util.OptionalDouble;

public class JSTokenizer {

    public JSTokenizer() {

    }

    private class JSTokenizerImpl {

        private JSCodeReader reader;
        private String buffer = "";
        private final JSTokenList tokens;

        public JSTokenizerImpl(JSCodeReader reader) throws JSTokenizeException {
            this.reader = reader;
            tokens = new JSTokenList();
            parse();
        }

        private void flush() {
            buffer = buffer.trim();
            if (buffer.equals("")) return;
            OptionalDouble optionalDouble = Utilities.parseDouble(buffer);
            if (optionalDouble.isPresent()) tokens.push(new JSNumToken(optionalDouble.getAsDouble()));
            else if (buffer.equals("function")) tokens.push(JSTokenType.FUNCTION);
            else if (buffer.equals("return")) tokens.push(JSTokenType.RETURN);
            else if (buffer.equals("null")) tokens.push(JSTokenType.NULL);
            else if (buffer.equals("undefined")) tokens.push(JSTokenType.UNDEFINED);
            else if (buffer.equals("let")) tokens.push(JSTokenType.LET);
            else tokens.push(new JSLiteralToken(buffer));
            buffer = "";
        }

        private void parse() throws JSTokenizeException {
            boolean inQuote = false;
            while (reader.canRead()) {
                char ch = reader.next();
                if (ch == '"' && inQuote) {
                    inQuote = false;
                    tokens.push(new JSLiteralToken(buffer));
                    buffer = "";
                    tokens.push(JSTokenType.END_STRING);
                    continue;
                }
                if (ch == '"' && !inQuote) {
                    flush();
                    tokens.push(JSTokenType.BEGIN_STRING);
                    inQuote = true;
                    continue;
                }
                if (inQuote) {
                    buffer += ch;
                    continue;
                }
                if (ch == ' ' || ch == '\n' || ch == '\t') {
                    flush();
                } else if (ch == '+') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.ADD));
                } else if (ch == '-') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.SUB));
                } else if (ch == '*') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.MUL));
                } else if (ch == '/') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.DIV));
                } else if (ch == '(') {
                    flush();
                    tokens.push(JSTokenType.LEFT_PAREN);
                } else if (ch == ')') {
                    flush();
                    tokens.push(JSTokenType.RIGHT_PAREN);
                } else if (ch == ';') {
                    flush();
                    tokens.push(JSTokenType.SEMICOLON);
                } else if (ch == '=') {
                    flush();
                    tokens.push(JSTokenType.EQUALS_SIGN);
                } else if (ch == '.') {
                    flush();
                    tokens.push(JSTokenType.DOT);
                } else if (ch == '{') {
                    flush();
                    tokens.push(JSTokenType.LEFT_BRACE);
                } else if (ch == '}') {
                    flush();
                    tokens.push(JSTokenType.RIGHT_BRACE);
                } else if (ch == ':') {
                    flush();
                    tokens.push(JSTokenType.COLON);
                } else if (ch == ',') {
                    flush();
                    tokens.push(JSTokenType.COMMA);
                } else if (ch == '[') {
                    flush();
                    tokens.push(JSTokenType.LEFT_BRACKET);
                } else if (ch == ']') {
                    flush();
                    tokens.push(JSTokenType.RIGHT_BRACKET);
                } else {
                    buffer += ch;
                }
            }
            flush();
        }
    }

    public JSTokenList tokenize(JSCodeReader reader) throws JSTokenizeException {
        JSTokenizerImpl impl = new JSTokenizerImpl(reader);
        return impl.tokens;
    }

    public JSTokenList tokenize(String code) throws JSTokenizeException {
        return tokenize(new JSCodeReader(code));
    }

}
