package p0nki.pesl.api.parse;

import p0nki.pesl.api.object.*;
import p0nki.pesl.api.token.PESLTokenList;
import p0nki.pesl.internal.nodes.*;
import p0nki.pesl.internal.token.type.*;

import javax.annotation.Nonnull;
import java.util.*;

public class PESLParser {
    public PESLParser() {

    }

    private ASTNode parseBracketedCode(PESLTokenList tokens, boolean catchReturn) throws PESLParseException {
        if (tokens.peek().getType() == TokenType.LEFT_BRACE) {
            tokens.expect(TokenType.LEFT_BRACE);
            List<ASTNode> nodes = new ArrayList<>();
            while (tokens.peek().getType() != TokenType.RIGHT_BRACE) {
                nodes.add(parseExpression(tokens));
            }
            tokens.expect(TokenType.RIGHT_BRACE);
            return new BodyNode(nodes, catchReturn);
        } else {
            return new BodyNode(Collections.singletonList(parseExpression(tokens)), catchReturn);
        }
    }

    private ASTNode parseAccess(ASTNode access, PESLTokenList tokens) throws PESLParseException {
        if (tokens.peek().getType() == TokenType.DOT || tokens.peek().getType() == TokenType.LEFT_BRACKET) {
            PESLToken accessor = tokens.pop();
            if (accessor.getType() == TokenType.DOT) {
                LiteralToken token1 = tokens.expect(TokenType.LITERAL);
                return new AccessPropertyNode(access, new LiteralNode(new StringObject(token1.getValue())));
            } else {
                ASTNode key = parseExpression(tokens);
                tokens.expect(TokenType.RIGHT_BRACKET);
                return new AccessPropertyNode(access, key);
            }
        }
        throw new PESLParseException(tokens.peek(), TokenType.DOT, TokenType.LEFT_BRACKET);
    }

    private ASTNode parseValue(PESLTokenList tokens) throws PESLParseException {
        TokenType top = tokens.peek().getType();
        if (top == TokenType.NOT) {
            tokens.expect(TokenType.NOT);
            return new NotNode(parsePrimary(tokens));
        } else if (top == TokenType.ADDITIVE_OP && ((OperatorToken) tokens.peek()).getOpType() == BiOperatorType.SUB) {
            tokens.expect(TokenType.ADDITIVE_OP);
            return new NegateNode(parsePrimary(tokens));
        } else if (top == TokenType.UNDEFINED) {
            tokens.expect(TokenType.UNDEFINED);
            return new LiteralNode(UndefinedObject.INSTANCE);
        } else if (top == TokenType.NULL) {
            tokens.expect(TokenType.NULL);
            return new LiteralNode(NullObject.INSTANCE);
        } else if (top == TokenType.TRUE) {
            tokens.expect(TokenType.TRUE);
            return new LiteralNode(new BooleanObject(true));
        } else if (top == TokenType.FALSE) {
            tokens.expect(TokenType.FALSE);
            return new LiteralNode(new BooleanObject(false));
        } else if (top == TokenType.RETURN) {
            tokens.expect(TokenType.RETURN);
            return new ReturnNode(parseExpression(tokens));
        } else if (top == TokenType.FUNCTION) {
            tokens.expect(TokenType.FUNCTION);
            tokens.expect(TokenType.LEFT_PAREN);
            List<String> argNames = new ArrayList<>();
            if (tokens.peek().getType() == TokenType.RIGHT_PAREN) {
                tokens.expect(TokenType.RIGHT_PAREN);
            } else {
                while (true) {
                    LiteralToken literalToken = tokens.expect(TokenType.LITERAL);
                    argNames.add(literalToken.getValue());
                    if (tokens.peek().getType() == TokenType.COMMA) {
                        tokens.expect(TokenType.COMMA);
                    } else {
                        tokens.expect(TokenType.RIGHT_PAREN);
                        break;
                    }
                }
            }
            return new FunctionDeclarationNode(argNames, parseBracketedCode(tokens, true));
        } else if (top == TokenType.TRY) {
            tokens.expect(TokenType.TRY);
            ASTNode tryBody = parseBracketedCode(tokens, false);
            tokens.expect(TokenType.CATCH);
            tokens.expect(TokenType.LEFT_PAREN);
            LiteralToken literal = tokens.expect(TokenType.LITERAL);
            tokens.expect(TokenType.RIGHT_PAREN);
            ASTNode catchBody = parseBracketedCode(tokens, false);
            return new TryNode(tryBody, literal.getValue(), catchBody);
        } else if (top == TokenType.NUMBER) {
            NumToken token = tokens.expect(TokenType.NUMBER);
            return new LiteralNode(new NumberObject(token.getValue()));
        } else if (top == TokenType.LEFT_PAREN) {
            tokens.expect(TokenType.LEFT_PAREN);
            ASTNode node = parseExpression(tokens);
            tokens.expect(TokenType.RIGHT_PAREN);
            return node;
        } else if (top == TokenType.FOR) {
            tokens.expect(TokenType.FOR);
            tokens.expect(TokenType.LEFT_PAREN);
            ASTNode init = parseExpression(tokens);
            tokens.expect(TokenType.SEMICOLON);
            ASTNode condition = parseExpression(tokens);
            tokens.expect(TokenType.SEMICOLON);
            ASTNode increment = parseExpression(tokens);
            tokens.expect(TokenType.RIGHT_PAREN);
            ASTNode body = parseBracketedCode(tokens, false);
            return new ForNode(init, condition, increment, body);
        } else if (top == TokenType.FOREACH) {
            tokens.expect(TokenType.FOREACH);
            tokens.expect(TokenType.LEFT_PAREN);
            LiteralToken varNameToken = tokens.expect(TokenType.LITERAL);
            String indexName = null;
            if (tokens.peek().getType() == TokenType.COMMA) {
                tokens.expect(TokenType.COMMA);
                LiteralToken indexNameToken = tokens.expect(TokenType.LITERAL);
                indexName = indexNameToken.getValue();
            }
            tokens.expect(TokenType.IN);
            ASTNode array = parseExpression(tokens);
            tokens.expect(TokenType.RIGHT_PAREN);
            ASTNode body = parseBracketedCode(tokens, false);
            return new ForEachNode(varNameToken.getValue(), indexName, array, body);
        } else if (top == TokenType.IF) {
            tokens.expect(TokenType.IF);
            tokens.expect(TokenType.LEFT_PAREN);
            ASTNode condition = parseExpression(tokens);
            tokens.expect(TokenType.RIGHT_PAREN);
            ASTNode then = parseBracketedCode(tokens, false);
            if (tokens.hasAny() && tokens.peek().getType() == TokenType.ELSE) {
                tokens.expect(TokenType.ELSE);
                return new IfNode(condition, then, parseBracketedCode(tokens, false));
            }
            return new IfNode(condition, then, null);
        } else if (top == TokenType.THROW) {
            tokens.expect(TokenType.THROW);
            return new ThrowNode(parseExpression(tokens));
        } else if (top == TokenType.LITERAL) {
            LiteralToken token = tokens.expect(TokenType.LITERAL);
            return new AccessPropertyNode(null, new LiteralNode(new StringObject(token.getValue())));
        } else if (top == TokenType.LEFT_BRACE) {
            tokens.expect(TokenType.LEFT_BRACE);
            if (tokens.consumeType(TokenType.RIGHT_BRACE)) return new MapNode(new HashMap<>());
            ASTNode firstKey = parseExpression(tokens);
            tokens.expect(TokenType.COLON);
            ASTNode firstValue = parseExpression(tokens);
            if (tokens.peek().getType() == TokenType.RIGHT_BRACE) {
                return new MapNode(new HashMap<ASTNode, ASTNode>() {{
                    put(firstKey, firstValue);
                }});
            } else if (tokens.peek().getType() == TokenType.COMMA) {
                Map<ASTNode, ASTNode> map = new HashMap<>();
                map.put(firstKey, firstValue);
                while (tokens.peek().getType() == TokenType.COMMA) {
                    tokens.expect(TokenType.COMMA);
                    ASTNode key = parseExpression(tokens);
                    tokens.expect(TokenType.COLON);
                    map.put(key, parseExpression(tokens));
                }
                tokens.expect(TokenType.RIGHT_BRACE);
                return new MapNode(map);
            } else if (tokens.peek().getType() == TokenType.FOR) {
                List<ComprehensionFor> fors = new ArrayList<>();
                while (tokens.peek().getType() == TokenType.FOR) {
                    tokens.expect(TokenType.FOR);
                    String first = tokens.literal();
                    if (tokens.peek().getType() == TokenType.COLON) {
                        tokens.expect(TokenType.COLON);
                        String second = tokens.literal();
                        tokens.expect(TokenType.IN);
                        ASTNode map = parseExpression(tokens);
                        fors.add(new ComprehensionFor(first, second, false, map));
                    } else if (tokens.peek().getType() == TokenType.COMMA) {
                        tokens.expect(TokenType.COMMA);
                        String second = tokens.literal();
                        tokens.expect(TokenType.IN);
                        ASTNode list = parseExpression(tokens);
                        fors.add(new ComprehensionFor(first, second, true, list));
                    } else {
                        tokens.expect(TokenType.IN);
                        ASTNode list = parseExpression(tokens);
                        fors.add(new ComprehensionFor(first, null, true, list));
                    }
                }
                ASTNode predicate = null;
                if (tokens.peek().getType() == TokenType.IF) {
                    tokens.expect(TokenType.IF);
                    predicate = parseExpression(tokens);
                }
                tokens.expect(TokenType.RIGHT_BRACE);
                return new MapComprehensionNode(firstKey, firstValue, fors, predicate);
            } else {
                tokens.expect(TokenType.RIGHT_BRACE, TokenType.COMMA, TokenType.FOR);
                throw new AssertionError("This will never be reached");
            }
        } else if (top == TokenType.LEFT_BRACKET) {
            tokens.expect(TokenType.LEFT_BRACKET);
            if (tokens.consumeType(TokenType.RIGHT_BRACKET)) {
                return new ArrayNode(new ArrayList<>());
            }
            ASTNode firstExpression = parseExpression(tokens);
            if (tokens.peek().getType() == TokenType.RIGHT_BRACKET) {
                return new ArrayNode(new ArrayList<ASTNode>() {{
                    add(firstExpression);
                }});
            } else if (tokens.peek().getType() == TokenType.COMMA) {
                List<ASTNode> list = new ArrayList<>();
                list.add(firstExpression);
                while (tokens.peek().getType() == TokenType.COMMA) {
                    tokens.expect(TokenType.COMMA);
                    list.add(parseExpression(tokens));
                }
                tokens.expect(TokenType.RIGHT_BRACKET);
                return new ArrayNode(list);
            } else if (tokens.peek().getType() == TokenType.FOR) {
                List<ComprehensionFor> fors = new ArrayList<>();
                while (tokens.peek().getType() == TokenType.FOR) {
                    tokens.expect(TokenType.FOR);
                    String first = tokens.literal();
                    if (tokens.peek().getType() == TokenType.COLON) {
                        tokens.expect(TokenType.COLON);
                        String second = tokens.literal();
                        tokens.expect(TokenType.IN);
                        ASTNode map = parseExpression(tokens);
                        fors.add(new ComprehensionFor(first, second, false, map));
                    } else if (tokens.peek().getType() == TokenType.COMMA) {
                        tokens.expect(TokenType.COMMA);
                        String second = tokens.literal();
                        tokens.expect(TokenType.IN);
                        ASTNode list = parseExpression(tokens);
                        fors.add(new ComprehensionFor(first, second, true, list));
                    } else {
                        tokens.expect(TokenType.IN);
                        ASTNode list = parseExpression(tokens);
                        fors.add(new ComprehensionFor(first, null, true, list));
                    }
                }
                ASTNode predicate = null;
                if (tokens.peek().getType() == TokenType.IF) {
                    tokens.expect(TokenType.IF);
                    predicate = parseExpression(tokens);
                }
                tokens.expect(TokenType.RIGHT_BRACKET);
                return new ArrayComprehensionNode(firstExpression, fors, predicate);
            } else {
                tokens.expect(TokenType.RIGHT_BRACKET, TokenType.COMMA, TokenType.FOR);
                throw new AssertionError("This will never be reached");
            }
        } else if (top == TokenType.BEGIN_STRING) {
            tokens.expect(TokenType.BEGIN_STRING);
            LiteralToken token = tokens.expect(TokenType.LITERAL);
            tokens.expect(TokenType.END_STRING);
            return new LiteralNode(new StringObject(token.getValue()));
        } else if (top == TokenType.DELETE) {
            tokens.expect(TokenType.DELETE);
            return new DeleteNode(parseExpression(tokens));
        } else if (top == TokenType.WHILE) {
            tokens.expect(TokenType.WHILE);
            tokens.expect(TokenType.LEFT_PAREN);
            ASTNode condition = parseExpression(tokens);
            tokens.expect(TokenType.RIGHT_PAREN);
            return new WhileNode(condition, parseBracketedCode(tokens, false));
        } else {
            throw new PESLParseException("Unexpected token in `primary` element " + tokens.peek().toString(), tokens.peek());
        }
    }

    private ASTNode parsePrimary(PESLTokenList tokens) throws PESLParseException {
        ASTNode node = parseValue(tokens);
        while (tokens.hasAny() && (tokens.peek().getType() == TokenType.DOT || tokens.peek().getType() == TokenType.LEFT_PAREN || tokens.peek().getType() == TokenType.LEFT_BRACKET)) {
            if (tokens.peek().getType() == TokenType.DOT || tokens.peek().getType() == TokenType.LEFT_BRACKET) {
                node = parseAccess(node, tokens);
            } else {
                tokens.expect(TokenType.LEFT_PAREN);
                List<ASTNode> arguments = new ArrayList<>();
                while (true) {
                    if (tokens.peek().getType() == TokenType.RIGHT_PAREN) {
                        tokens.expect(TokenType.RIGHT_PAREN);
                        break;
                    }
                    arguments.add(parseExpression(tokens));
                    if (tokens.peek().getType() == TokenType.COMMA) {
                        tokens.expect(TokenType.COMMA);
                    } else {
                        tokens.expect(TokenType.RIGHT_PAREN);
                        break;
                    }
                }
                node = new FunctionInvokeNode(node, arguments);
            }
        }
        return node;
    }

    private ASTNode parseMultiplicative(PESLTokenList tokens) throws PESLParseException {
        ASTNode left = parsePrimary(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == TokenType.MULTIPLICATIVE_OP) {
            OperatorToken token = tokens.expect(TokenType.MULTIPLICATIVE_OP);
            left = new OperatorNode(new ASTNode[]{left, parsePrimary(tokens)}, token.getOpType());
        }
        return left;
    }

    private ASTNode parseAdditive(PESLTokenList tokens) throws PESLParseException {
        ASTNode left = parseMultiplicative(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == TokenType.ADDITIVE_OP) {
            OperatorToken token = tokens.expect(TokenType.ADDITIVE_OP);
            left = new OperatorNode(new ASTNode[]{left, parseMultiplicative(tokens)}, token.getOpType());
        }
        return left;
    }

    private ASTNode parseComparative(PESLTokenList tokens) throws PESLParseException {
        ASTNode left = parseAdditive(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == TokenType.COMPARATIVE_OP) {
            OperatorToken token = tokens.expect(TokenType.COMPARATIVE_OP);
            left = new OperatorNode(new ASTNode[]{left, parseAdditive(tokens)}, token.getOpType());
        }
        return left;
    }

    private ASTNode parseBoolean(PESLTokenList tokens) throws PESLParseException {
        ASTNode left = parseComparative(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == TokenType.BOOLEAN_OP) {
            OperatorToken token = tokens.expect(TokenType.BOOLEAN_OP);
            left = new OperatorNode(new ASTNode[]{left, parseComparative(tokens)}, token.getOpType());
        }
        return left;
    }

    @Nonnull
    public ASTNode parseExpression(@Nonnull PESLTokenList tokens) throws PESLParseException {
        boolean let = tokens.peek().getType() == TokenType.LET;
        if (let) tokens.expect(TokenType.LET);
        ASTNode left = parseBoolean(tokens);
        if (tokens.hasAny()) {
            if (tokens.peek().getType() == TokenType.ASSIGNMENT_OP) {
                AssignmentOpToken opToken = tokens.expect(TokenType.ASSIGNMENT_OP);
                return new EqualsNode(opToken.getOpType(), left, parseExpression(tokens), let);
            }
        }
        return left;
    }

}
