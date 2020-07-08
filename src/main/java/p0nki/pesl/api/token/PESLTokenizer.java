package p0nki.pesl.api.token;

import p0nki.pesl.internal.token.type.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class PESLTokenizer {

    public PESLTokenizer() {

    }

    @Nonnull
    public PESLTokenList tokenize(PESLCodeReader reader) throws PESLTokenizeException {
        TokenizerImpl impl = new TokenizerImpl(reader);
        return new PESLTokenList(impl.tokens);
    }

    @Nonnull
    public PESLTokenList tokenize(String code) throws PESLTokenizeException {
        return tokenize(new PESLCodeReader(code));
    }

    // NOTE: this will be un-static-ed if I add tokenizer options
    private static class TokenizerImpl {

        private final PESLCodeReader reader;
        private List<PESLToken> tokens;
        private StringBuilder buffer = new StringBuilder();

        public TokenizerImpl(PESLCodeReader reader) throws PESLTokenizeException {
            this.reader = reader;
            tokens = new ArrayList<>();
            parse();
            postProcess();
        }

        private int start() {
            return reader.getIndex() - buffer.length();
        }

        private OptionalLong parseSingleLong(String buffer) {
            long value = 0;
            int digits = 0;
            while (buffer.length() > 0) {
                char ch = buffer.charAt(0);
                buffer = buffer.substring(1);
                if ('0' <= ch && ch <= '9') {
                    value *= 10;
                    value += ch - '0';
                    digits++;
                } else {
                    return OptionalLong.empty();
                }
            }
            if (digits == 0) return OptionalLong.empty();
            return OptionalLong.of(value);
        }

        private OptionalLong parseSingleLong() throws PESLTokenizeException {
            long value = 0;
            int digits = 0;
            while (reader.canRead()) {
                char ch = reader.next();
                if ('0' <= ch && ch <= '9') {
                    value *= 10;
                    value += ch - '0';
                    digits++;
                } else {
                    break;
                }
            }
            if (digits == 0) return OptionalLong.empty();
            return OptionalLong.of(value);
        }

        private void flush() {
            buffer = new StringBuilder(buffer.toString().trim());
            if (buffer.toString().equals("")) return;
            OptionalLong optionalLong = parseSingleLong(buffer.toString());
            if (optionalLong.isPresent()) {
                tokens.add(new NumToken(optionalLong.getAsLong(), start() + 1, reader.getIndex() + 1, false));
            } else {
                switch (buffer.toString()) {
                    case "function":
                        tokens.add(new PESLToken(TokenType.FUNCTION, start() + 1, reader.getIndex() + 1));
                        break;
                    case "return":
                        tokens.add(new PESLToken(TokenType.RETURN, start() + 1, reader.getIndex() + 1));
                        break;
                    case "null":
                        tokens.add(new PESLToken(TokenType.NULL, start() + 1, reader.getIndex() + 1));
                        break;
                    case "undefined":
                        tokens.add(new PESLToken(TokenType.UNDEFINED, start() + 1, reader.getIndex() + 1));
                        break;
                    case "let":
                        tokens.add(new PESLToken(TokenType.LET, start() + 1, reader.getIndex() + 1));
                        break;
                    case "is":
                        tokens.add(new OperatorToken(BiOperatorType.EQUIVALENCE, start() + 1, reader.getIndex() + 1));
                        break;
                    case "for":
                        tokens.add(new PESLToken(TokenType.FOR, start() + 1, reader.getIndex() + 1));
                        break;
                    case "if":
                        tokens.add(new PESLToken(TokenType.IF, start() + 1, reader.getIndex() + 1));
                        break;
                    case "else":
                        tokens.add(new PESLToken(TokenType.ELSE, start() + 1, reader.getIndex() + 1));
                        break;
                    case "true":
                        tokens.add(new PESLToken(TokenType.TRUE, start() + 1, reader.getIndex() + 1));
                        break;
                    case "false":
                        tokens.add(new PESLToken(TokenType.FALSE, start() + 1, reader.getIndex() + 1));
                        break;
                    case "throw":
                        tokens.add(new PESLToken(TokenType.THROW, start() + 1, reader.getIndex() + 1));
                        break;
                    case "try":
                        tokens.add(new PESLToken(TokenType.TRY, start() + 1, reader.getIndex() + 1));
                        break;
                    case "catch":
                        tokens.add(new PESLToken(TokenType.CATCH, start() + 1, reader.getIndex() + 1));
                        break;
                    case "foreach":
                        tokens.add(new PESLToken(TokenType.FOREACH, start() + 1, reader.getIndex() + 1));
                        break;
                    case "delete":
                        tokens.add(new PESLToken(TokenType.DELETE, start() + 1, reader.getIndex() + 1));
                        break;
                    case "while":
                        tokens.add(new PESLToken(TokenType.WHILE, start() + 1, reader.getIndex() + 1));
                        break;
                    case "in":
                        tokens.add(new PESLToken(TokenType.IN, start() + 1, reader.getIndex() + 1));
                        break;
                    default:
                        tokens.add(new LiteralToken(buffer.toString(), start() + 1, reader.getIndex() + 1));
                        break;
                }
            }
            buffer = new StringBuilder();
        }

        private void collapseBuffer(List<PESLToken> buffer) {
            if (buffer.size() < 2) return;
            PESLToken first = buffer.get(buffer.size() - 2);
            PESLToken second = buffer.get(buffer.size() - 1);
            if (first instanceof AssignmentOpToken && ((AssignmentOpToken) first).getOpType() == AssignmentType.EQUALS && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new OperatorToken(BiOperatorType.EQUIVALENCE, first.getStart(), second.getEnd()));
            } else if (first instanceof OperatorToken && ((OperatorToken) first).getOpType() == BiOperatorType.LESS_THAN && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new OperatorToken(BiOperatorType.LESS_THAN_OR_EQUAL_TO, first.getStart(), second.getEnd()));
            } else if (first instanceof OperatorToken && ((OperatorToken) first).getOpType() == BiOperatorType.MORE_THAN && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new OperatorToken(BiOperatorType.MORE_THAN_OR_EQUAL_TO, first.getStart(), second.getEnd()));
            } else if (first instanceof OperatorToken && ((OperatorToken) first).getOpType() == BiOperatorType.ADD && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new AssignmentOpToken(AssignmentType.PLUS_EQUALS, first.getStart(), second.getEnd()));
            } else if (first instanceof OperatorToken && ((OperatorToken) first).getOpType() == BiOperatorType.SUB && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new AssignmentOpToken(AssignmentType.MINUS_EQUALS, first.getStart(), second.getEnd()));
            } else if (first instanceof OperatorToken && ((OperatorToken) first).getOpType() == BiOperatorType.MUL && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new AssignmentOpToken(AssignmentType.TIMES_EQUALS, first.getStart(), second.getEnd()));
            } else if (first instanceof OperatorToken && ((OperatorToken) first).getOpType() == BiOperatorType.DIV && second instanceof AssignmentOpToken && ((AssignmentOpToken) second).getOpType() == AssignmentType.EQUALS) {
                buffer.remove(buffer.size() - 1);
                buffer.remove(buffer.size() - 1);
                buffer.add(new AssignmentOpToken(AssignmentType.DIVIDES_EQUALS, first.getStart(), second.getEnd()));
            }
        }

        private void postProcess() {
            List<PESLToken> buffer = new ArrayList<>();
            for (PESLToken token : tokens) {
                buffer.add(token);
                collapseBuffer(buffer);
            }
            tokens = buffer;
        }

        private void parse() throws PESLTokenizeException {
            boolean inQuote = false;
            while (reader.canRead()) {
                char ch = reader.next();
                if (ch == '"' && inQuote) {
                    inQuote = false;
                    tokens.add(new LiteralToken(buffer.toString(), reader.getIndex(), reader.getIndex() + 1));
                    buffer = new StringBuilder();
                    tokens.add(new PESLToken(TokenType.END_STRING, reader.getIndex(), reader.getIndex() + 1));
                    continue;
                }
                if (ch == '"') {
                    flush();
                    tokens.add(new PESLToken(TokenType.BEGIN_STRING, reader.getIndex(), reader.getIndex() + 1));
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
                    tokens.add(new PESLToken(TokenType.NOT, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '+') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.ADD, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '-') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.SUB, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '*') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.MUL, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '/') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.DIV, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '&') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.AND, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '|') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.OR, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '^') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.XOR, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '<') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.LESS_THAN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '>') {
                    flush();
                    tokens.add(new OperatorToken(BiOperatorType.MORE_THAN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '(') {
                    flush();
                    tokens.add(new PESLToken(TokenType.LEFT_PAREN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == ')') {
                    flush();
                    tokens.add(new PESLToken(TokenType.RIGHT_PAREN, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == ';') {
                    flush();
                    tokens.add(new PESLToken(TokenType.SEMICOLON, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '=') {
                    flush();
                    tokens.add(new AssignmentOpToken(AssignmentType.EQUALS, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '.') {
                    flush();
                    if (tokens.size() > 0 && tokens.get(tokens.size() - 1).getType() == TokenType.NUMBER) {
                        NumToken first = (NumToken) tokens.get(tokens.size() - 1);
                        if (!first.isHasFP()) {
                            OptionalLong nextLong = parseSingleLong();
                            if (nextLong.isPresent()) {
                                PESLToken original = tokens.remove(tokens.size() - 1);
                                tokens.add(new NumToken(Double.parseDouble(((int) first.getValue()) + "." + nextLong.getAsLong()), original.getStart(), reader.getIndex() + 1, true));
                                flush();
                                reader.setIndex(reader.getIndex() - 1);
                                continue;
                            }
                        }
                    }
                    tokens.add(new PESLToken(TokenType.DOT, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '{') {
                    flush();
                    tokens.add(new PESLToken(TokenType.LEFT_BRACE, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '}') {
                    flush();
                    tokens.add(new PESLToken(TokenType.RIGHT_BRACE, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == ':') {
                    flush();
                    tokens.add(new PESLToken(TokenType.COLON, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == ',') {
                    flush();
                    tokens.add(new PESLToken(TokenType.COMMA, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == '[') {
                    flush();
                    tokens.add(new PESLToken(TokenType.LEFT_BRACKET, reader.getIndex(), reader.getIndex() + 1));
                } else if (ch == ']') {
                    flush();
                    tokens.add(new PESLToken(TokenType.RIGHT_BRACKET, reader.getIndex(), reader.getIndex() + 1));
                } else {
                    buffer.append(ch);
                }
            }
            flush();
        }
    }

}
