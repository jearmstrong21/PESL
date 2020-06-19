package p0nki.javashit.ast;

import p0nki.javashit.ast.nodes.*;
import p0nki.javashit.object.*;
import p0nki.javashit.token.JSTokenList;
import p0nki.javashit.token.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSASTCreator {

    public JSASTCreator() {

    }

    private JSASTNode parseBracketedCode(JSTokenList tokens, boolean catchReturn) throws JSParseException {
        tokens.expect(JSTokenType.LEFT_BRACE);
        List<JSASTNode> nodes = new ArrayList<>();
        while (tokens.peek().getType() != JSTokenType.RIGHT_BRACE) {
            nodes.add(parseExpression(tokens));
//            if (tokens.peek().getType() == JSTokenType.SEMICOLON) tokens.expect(JSTokenType.SEMICOLON);
        }
        tokens.expect(JSTokenType.RIGHT_BRACE);
        return new JSBodyNode(nodes, catchReturn);
    }

    private JSASTNode parsePrimary1(JSTokenList tokens) throws JSParseException {
        JSTokenType top = tokens.peek().getType();
        if (top == JSTokenType.UNDEFINED) {
            tokens.expect(JSTokenType.UNDEFINED);
            return new JSLiteralNode(JSUndefinedObject.INSTANCE);
        } else if (top == JSTokenType.NULL) {
            tokens.expect(JSTokenType.NULL);
            return new JSLiteralNode(JSNullObject.INSTANCE);
        } else if (top == JSTokenType.TRUE) {
            tokens.expect(JSTokenType.TRUE);
            return new JSLiteralNode(new JSBooleanObject(true));
        } else if (top == JSTokenType.FALSE) {
            tokens.expect(JSTokenType.FALSE);
            return new JSLiteralNode(new JSBooleanObject(false));
        } else if (top == JSTokenType.RETURN) {
            tokens.expect(JSTokenType.RETURN);
            return new JSReturnNode(parseExpression(tokens));
        } else if (top == JSTokenType.FUNCTION) {
            tokens.expect(JSTokenType.FUNCTION);
            tokens.expect(JSTokenType.LEFT_PAREN);
            List<String> argNames = new ArrayList<>();
            if (tokens.peek().getType() == JSTokenType.RIGHT_PAREN) {
                tokens.expect(JSTokenType.RIGHT_PAREN);
            } else {
                while (true) {
                    JSLiteralToken literalToken = tokens.expect(JSTokenType.LITERAL);
                    argNames.add(literalToken.getValue());
                    if (tokens.peek().getType() == JSTokenType.COMMA) {
                        tokens.expect(JSTokenType.COMMA);
                    } else {
                        tokens.expect(JSTokenType.RIGHT_PAREN);
                        break;
                    }
                }
            }
            return new JSLiteralNode(new JSFunction(argNames, parseBracketedCode(tokens, true)));
        } else if (top == JSTokenType.NUMBER) {
            JSNumToken token = tokens.expect(JSTokenType.NUMBER);
            return new JSLiteralNode(new JSNumberObject(token.getValue()));
        } else if (top == JSTokenType.LEFT_PAREN) {
            tokens.expect(JSTokenType.LEFT_PAREN);
            JSASTNode node = parseExpression(tokens);
            tokens.expect(JSTokenType.RIGHT_PAREN);
            return node;
        } else if (top == JSTokenType.FOR) {
            tokens.expect(JSTokenType.FOR);
            tokens.expect(JSTokenType.LEFT_PAREN);
            JSASTNode first = parseExpression(tokens);
            if (tokens.peek().getType() == JSTokenType.COLON) {
                throw new UnsupportedOperationException("For-in loops are not yet supported");
            }
            tokens.expect(JSTokenType.SEMICOLON);
            JSASTNode condition = parseExpression(tokens);
            tokens.expect(JSTokenType.SEMICOLON);
            JSASTNode increment = parseExpression(tokens);
            tokens.expect(JSTokenType.RIGHT_PAREN);
            JSASTNode body = parseBracketedCode(tokens, false);
            return new JSForNode(first, condition, increment, body);
        } else if (top == JSTokenType.IF) {
            tokens.expect(JSTokenType.IF);
            tokens.expect(JSTokenType.LEFT_PAREN);
            JSASTNode condition = parseExpression(tokens);
            tokens.expect(JSTokenType.RIGHT_PAREN);
            JSASTNode then = parseBracketedCode(tokens, false);
            if (tokens.hasAny() && tokens.peek().getType() == JSTokenType.ELSE) {
                tokens.expect(JSTokenType.ELSE);
                return new JSIfNode(condition, then, parseBracketedCode(tokens, false));
            }
            return new JSIfNode(condition, then, null);
        } else if (top == JSTokenType.LITERAL) {
            JSLiteralToken token = tokens.expect(JSTokenType.LITERAL);
            JSASTNode access = new JSAccessPropertyNode(null, new JSLiteralNode(new JSStringObject(token.getValue())));
            while (tokens.hasAny() && (tokens.peek().getType() == JSTokenType.DOT || tokens.peek().getType() == JSTokenType.LEFT_BRACKET)) {
                JSToken accessor = tokens.pop();
                if (accessor.getType() == JSTokenType.DOT) {
                    JSLiteralToken token1 = tokens.expect(JSTokenType.LITERAL);
                    access = new JSAccessPropertyNode(access, new JSLiteralNode(new JSStringObject(token1.getValue())));
                } else {
                    JSASTNode key = parseExpression(tokens);
                    tokens.expect(JSTokenType.RIGHT_BRACKET);
                    access = new JSAccessPropertyNode(access, key);
                }
            }
            return access;
        } else if (top == JSTokenType.LEFT_BRACE) {
            tokens.expect(JSTokenType.LEFT_BRACE);
            Map<String, JSASTNode> map = new HashMap<>();
            while (true) {
                boolean quotedKey = false;
                if (tokens.peek().getType() == JSTokenType.BEGIN_STRING) {
                    quotedKey = true;
                    tokens.expect(JSTokenType.BEGIN_STRING);
                }
                JSLiteralToken token = tokens.expect(JSTokenType.LITERAL);
                if (quotedKey) tokens.expect(JSTokenType.END_STRING);
                tokens.expect(JSTokenType.COLON);
                JSASTNode value = parseExpression(tokens);
                map.put(token.getValue(), value);
                if (tokens.peek().getType() == JSTokenType.RIGHT_BRACE) break;
                tokens.expect(JSTokenType.COMMA);
            }
            tokens.expect(JSTokenType.RIGHT_BRACE);
            return new JSMapNode(map);
        } else if (top == JSTokenType.LEFT_BRACKET) {
            tokens.expect(JSTokenType.LEFT_BRACKET);
            List<JSASTNode> list = new ArrayList<>();
            while (true) {
                if (tokens.peek().getType() == JSTokenType.RIGHT_BRACKET) break;
                list.add(parseExpression(tokens));
                if (tokens.peek().getType() == JSTokenType.RIGHT_BRACKET) break;
                tokens.expect(JSTokenType.COMMA);
            }
            tokens.expect(JSTokenType.RIGHT_BRACKET);
            return new JSArrayNode(list);
        } else if (top == JSTokenType.BEGIN_STRING) {
            tokens.expect(JSTokenType.BEGIN_STRING);
            JSLiteralToken token = tokens.expect(JSTokenType.LITERAL);
            tokens.expect(JSTokenType.END_STRING);
            return new JSLiteralNode(new JSStringObject(token.getValue()));
        } else {
            throw new UnsupportedOperationException(top.toString());
        }
    }

    private JSASTNode parsePrimary(JSTokenList tokens) throws JSParseException {
        JSASTNode node = parsePrimary1(tokens);
        if (tokens.hasAny()) {
            if (tokens.peek().getType() == JSTokenType.LEFT_PAREN) {
                tokens.expect(JSTokenType.LEFT_PAREN);
                List<JSASTNode> arguments = new ArrayList<>();
                while (true) {
                    if (tokens.peek().getType() == JSTokenType.RIGHT_PAREN) {
                        tokens.expect(JSTokenType.RIGHT_PAREN);
                        break;
                    }
                    arguments.add(parseExpression(tokens));
                    if (tokens.peek().getType() == JSTokenType.COMMA) {
                        tokens.expect(JSTokenType.COMMA);
                    } else {
                        tokens.expect(JSTokenType.RIGHT_PAREN);
                        break;
                    }
                }
                return new JSFunctionInvokeNode(node, arguments);
            }
        }
        return node;
    }

    private JSASTNode parseMultiplicative(JSTokenList tokens) throws JSParseException {
        JSASTNode left = parsePrimary(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == JSTokenType.MULTIPLICATIVE_OP) {
            JSOperatorToken token = tokens.expect(JSTokenType.MULTIPLICATIVE_OP);
            left = new JSOperatorNode(new JSASTNode[]{left, parsePrimary(tokens)}, token.getOpType());
        }
        return left;
    }

    private JSASTNode parseAdditive(JSTokenList tokens) throws JSParseException {
        JSASTNode left = parseMultiplicative(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == JSTokenType.ADDITIVE_OP) {
            JSOperatorToken token = tokens.expect(JSTokenType.ADDITIVE_OP);
            left = new JSOperatorNode(new JSASTNode[]{left, parseMultiplicative(tokens)}, token.getOpType());
        }
        return left;
    }

    private JSASTNode parseComparative(JSTokenList tokens) throws JSParseException {
        JSASTNode left = parseAdditive(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == JSTokenType.COMPARATIVE_OP) {
            JSOperatorToken token = tokens.expect(JSTokenType.COMPARATIVE_OP);
            left = new JSOperatorNode(new JSASTNode[]{left, parseAdditive(tokens)}, token.getOpType());
        }
        return left;
    }

    private JSASTNode parseBoolean(JSTokenList tokens) throws JSParseException {
        JSASTNode left = parseComparative(tokens);
        while (tokens.hasAny() && tokens.peek().getType() == JSTokenType.BOOLEAN_OP) {
            JSOperatorToken token = tokens.expect(JSTokenType.BOOLEAN_OP);
            left = new JSOperatorNode(new JSASTNode[]{left, parseComparative(tokens)}, token.getOpType());
        }
        return left;
    }

    public JSASTNode parseExpression(JSTokenList tokens) throws JSParseException {
        boolean let = tokens.peek().getType() == JSTokenType.LET;
        if (let) tokens.expect(JSTokenType.LET);
        JSASTNode left = parseBoolean(tokens);
        if (tokens.hasAny()) {
            if (tokens.peek().getType() == JSTokenType.EQUALS_SIGN) {
                tokens.expect(JSTokenType.EQUALS_SIGN);
                JSASTNode right = parseBoolean(tokens);
                if (left instanceof JSAccessPropertyNode) {
                    JSASTNode value = ((JSAccessPropertyNode) left).getValue();
                    JSASTNode key = ((JSAccessPropertyNode) left).getKey();
                    return new JSEqualsNode(value, key, right, let);
                } else {
                    throw new JSParseException("Expected lvalue on left side of `=`");
                }
            }
        }
        if (let) throw new JSParseException("Expected assignment with `let`");
        return left;
    }

}
