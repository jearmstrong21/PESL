package p0nki.javashit.token;

import p0nki.javashit.Utilities;
import p0nki.javashit.token.type.*;

import java.util.OptionalDouble;

public class JSTokenizer {

    public JSTokenizer() {

    }

    private class JSTokenizerImpl {

        private final JSCodeReader reader;
        private String buffer = "";
        private final JSTokenList tokens;

        public JSTokenizerImpl(JSCodeReader reader) throws JSTokenizeException {
            this.reader = reader;
            tokens = new JSTokenList();
            parse();
        }

        private int start() {
            return reader.getIndex() - buffer.length();
        }

        private void flush() {
            buffer = buffer.trim();
            if (buffer.equals("")) return;
            OptionalDouble optionalDouble = Utilities.parseDouble(buffer);
            if (optionalDouble.isPresent())
                tokens.push(new JSNumToken(optionalDouble.getAsDouble(), start() + 1, reader.getIndex() + 1));
            else if (buffer.equals("function")) tokens.push(JSTokenType.FUNCTION, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("return")) tokens.push(JSTokenType.RETURN, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("null")) tokens.push(JSTokenType.NULL, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("undefined")) tokens.push(JSTokenType.UNDEFINED, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("let")) tokens.push(JSTokenType.LET, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("is"))
                tokens.push(new JSOperatorToken(JSOperatorType.EQUALS, start() + 1, reader.getIndex() + 1));
            else if (buffer.equals("for")) tokens.push(JSTokenType.FOR, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("if")) tokens.push(JSTokenType.IF, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("else")) tokens.push(JSTokenType.ELSE, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("true")) tokens.push(JSTokenType.TRUE, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("false")) tokens.push(JSTokenType.FALSE, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("throw")) tokens.push(JSTokenType.THROW, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("try")) tokens.push(JSTokenType.TRY, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("catch")) tokens.push(JSTokenType.CATCH, start() + 1, reader.getIndex() + 1);
            else if (buffer.equals("foreach")) tokens.push(JSTokenType.FOREACH, start() + 1, reader.getIndex() + 1);
            else tokens.push(new JSLiteralToken(buffer, start() + 1, reader.getIndex() + 1));
            buffer = "";
        }

        private void parse() throws JSTokenizeException {
            boolean inQuote = false;
            while (reader.canRead()) {
                char ch = reader.next();
                if (ch == '"' && inQuote) {
                    inQuote = false;
                    tokens.push(new JSLiteralToken(buffer, reader.getIndex(), reader.getIndex() + 1));
                    buffer = "";
                    tokens.push(JSTokenType.END_STRING, reader.getIndex(), reader.getIndex() + 1);
                    continue;
                }
                if (ch == '"') {
                    flush();
                    tokens.push(JSTokenType.BEGIN_STRING, reader.getIndex(), reader.getIndex() + 1);
                    inQuote = true;
                    continue;
                }
                if (inQuote) {
                    buffer += ch;
                    continue;
                }
                if (ch == ' ' || ch == '\n' || ch == '\t') {
                    flush();
                } else if (ch == '!') {
                    flush();
                    tokens.push(JSTokenType.NOT, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '+') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.ADD, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '-') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.SUB, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '*') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.MUL, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '/') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.DIV, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '&') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.AND, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '|') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.OR, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '^') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.XOR, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '<') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.LESS_THAN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '>') {
                    flush();
                    tokens.push(new JSOperatorToken(JSOperatorType.MORE_THAN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '(') {
                    flush();
                    tokens.push(JSTokenType.LEFT_PAREN, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ')') {
                    flush();
                    tokens.push(JSTokenType.RIGHT_PAREN, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ';') {
                    flush();
                    tokens.push(JSTokenType.SEMICOLON, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '=') {
                    flush();
                    tokens.push(JSTokenType.EQUALS_SIGN, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '.') {
                    flush();
                    tokens.push(JSTokenType.DOT, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '{') {
                    flush();
                    tokens.push(JSTokenType.LEFT_BRACE, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '}') {
                    flush();
                    tokens.push(JSTokenType.RIGHT_BRACE, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ':') {
                    flush();
                    tokens.push(JSTokenType.COLON, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ',') {
                    flush();
                    tokens.push(JSTokenType.COMMA, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '[') {
                    flush();
                    tokens.push(JSTokenType.LEFT_BRACKET, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ']') {
                    flush();
                    tokens.push(JSTokenType.RIGHT_BRACKET, reader.getIndex(), reader.getIndex() + 1);
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
