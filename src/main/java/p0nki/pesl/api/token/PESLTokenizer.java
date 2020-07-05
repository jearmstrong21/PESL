package p0nki.pesl.api.token;

import p0nki.pesl.internal.token.type.*;

import javax.annotation.Nonnull;

public class PESLTokenizer {

    public PESLTokenizer() {

    }

    @Nonnull
    public PESLTokenList tokenize(PESLCodeReader reader) throws PESLTokenizeException {
        JSTokenizerImpl impl = new JSTokenizerImpl(reader);
        return impl.tokens;
    }

    @Nonnull
    public PESLTokenList tokenize(String code) throws PESLTokenizeException {
        return tokenize(new PESLCodeReader(code));
    }

    // NOTE: this will be un-static-ed if I add tokenizer options
    private static class JSTokenizerImpl {

        private final PESLCodeReader reader;
        private final PESLTokenList tokens;
        private StringBuilder buffer = new StringBuilder();

        public JSTokenizerImpl(PESLCodeReader reader) throws PESLTokenizeException {
            this.reader = reader;
            tokens = new PESLTokenList();
            parse();
        }

        private int start() {
            return reader.getIndex() - buffer.length();
        }

        private void flush() {
            buffer = new StringBuilder(buffer.toString().trim());
            if (buffer.toString().equals("")) return;
            try {
                tokens.push(new NumToken(Double.parseDouble(buffer.toString()), start() + 1, reader.getIndex() + 1));
            } catch (NumberFormatException ignored) {
                switch (buffer.toString()) {
                    case "function":
                        tokens.push(TokenType.FUNCTION, start() + 1, reader.getIndex() + 1);
                        break;
                    case "return":
                        tokens.push(TokenType.RETURN, start() + 1, reader.getIndex() + 1);
                        break;
                    case "null":
                        tokens.push(TokenType.NULL, start() + 1, reader.getIndex() + 1);
                        break;
                    case "undefined":
                        tokens.push(TokenType.UNDEFINED, start() + 1, reader.getIndex() + 1);
                        break;
                    case "let":
                        tokens.push(TokenType.LET, start() + 1, reader.getIndex() + 1);
                        break;
                    case "is":
                        tokens.push(new OperatorToken(OperatorType.EQUALS, start() + 1, reader.getIndex() + 1));
                        break;
                    case "for":
                        tokens.push(TokenType.FOR, start() + 1, reader.getIndex() + 1);
                        break;
                    case "if":
                        tokens.push(TokenType.IF, start() + 1, reader.getIndex() + 1);
                        break;
                    case "else":
                        tokens.push(TokenType.ELSE, start() + 1, reader.getIndex() + 1);
                        break;
                    case "true":
                        tokens.push(TokenType.TRUE, start() + 1, reader.getIndex() + 1);
                        break;
                    case "false":
                        tokens.push(TokenType.FALSE, start() + 1, reader.getIndex() + 1);
                        break;
                    case "throw":
                        tokens.push(TokenType.THROW, start() + 1, reader.getIndex() + 1);
                        break;
                    case "try":
                        tokens.push(TokenType.TRY, start() + 1, reader.getIndex() + 1);
                        break;
                    case "catch":
                        tokens.push(TokenType.CATCH, start() + 1, reader.getIndex() + 1);
                        break;
                    case "foreach":
                        tokens.push(TokenType.FOREACH, start() + 1, reader.getIndex() + 1);
                        break;
                    case "delete":
                        tokens.push(TokenType.DELETE, start() + 1, reader.getIndex() + 1);
                        break;
                    default:
                        tokens.push(new LiteralToken(buffer.toString(), start() + 1, reader.getIndex() + 1));
                        break;
                }
            }
            buffer = new StringBuilder();
        }

        private void parse() throws PESLTokenizeException {
            boolean inQuote = false;
            while (reader.canRead()) {
                char ch = reader.next();
                if (ch == '"' && inQuote) {
                    inQuote = false;
                    tokens.push(new LiteralToken(buffer.toString(), reader.getIndex(), reader.getIndex() + 1));
                    buffer = new StringBuilder();
                    tokens.push(TokenType.END_STRING, reader.getIndex(), reader.getIndex() + 1);
                    continue;
                }
                if (ch == '"') {
                    flush();
                    tokens.push(TokenType.BEGIN_STRING, reader.getIndex(), reader.getIndex() + 1);
                    inQuote = true;
                    continue;
                }
                if (inQuote) {
                    buffer.append(ch);
                    continue;
                }
                if (ch == ' ' || ch == '\n' || ch == '\t') {
                    flush();
                } else if (ch == '!') {
                    flush();
                    tokens.push(TokenType.NOT, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '+') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.ADD, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '-') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.SUB, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '*') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.MUL, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '/') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.DIV, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '&') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.AND, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '|') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.OR, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '^') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.XOR, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '<') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.LESS_THAN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '>') {
                    flush();
                    tokens.push(new OperatorToken(OperatorType.MORE_THAN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '(') {
                    flush();
                    tokens.push(TokenType.LEFT_PAREN, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ')') {
                    flush();
                    tokens.push(TokenType.RIGHT_PAREN, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ';') {
                    flush();
                    tokens.push(TokenType.SEMICOLON, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '=') {
                    flush();
                    tokens.push(TokenType.EQUALS_SIGN, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '.') {
                    flush();
                    tokens.push(TokenType.DOT, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '{') {
                    flush();
                    tokens.push(TokenType.LEFT_BRACE, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '}') {
                    flush();
                    tokens.push(TokenType.RIGHT_BRACE, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ':') {
                    flush();
                    tokens.push(TokenType.COLON, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ',') {
                    flush();
                    tokens.push(TokenType.COMMA, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == '[') {
                    flush();
                    tokens.push(TokenType.LEFT_BRACKET, reader.getIndex(), reader.getIndex() + 1);
                } else if (ch == ']') {
                    flush();
                    tokens.push(TokenType.RIGHT_BRACKET, reader.getIndex(), reader.getIndex() + 1);
                } else {
                    buffer.append(ch);
                }
            }
            flush();
        }
    }

}
