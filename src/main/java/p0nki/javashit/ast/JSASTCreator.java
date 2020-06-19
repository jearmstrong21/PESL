package p0nki.javashit.ast;

import p0nki.javashit.ast.nodes.*;
import p0nki.javashit.object.JSNumberObject;
import p0nki.javashit.object.JSStringObject;
import p0nki.javashit.token.JSTokenList;
import p0nki.javashit.token.type.*;

import java.util.HashMap;
import java.util.Map;

public class JSASTCreator {

    public JSASTCreator() {

    }

    private JSASTNode parsePrimary(JSTokenList tokens) throws JSParseException {
        JSTokenType top = tokens.peek().getType();
        if (top == JSTokenType.NUMBER) {
            JSNumToken token = tokens.expect(JSTokenType.NUMBER);
            return new JSLiteralNode(new JSNumberObject(token.getValue()));
        } else if (top == JSTokenType.LEFT_PAREN) {
            tokens.expect(JSTokenType.LEFT_PAREN);
            JSASTNode node = parseAdditive(tokens);
            tokens.expect(JSTokenType.RIGHT_PAREN);
            return node;
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
        } else if (top == JSTokenType.BEGIN_STRING) {
            tokens.expect(JSTokenType.BEGIN_STRING);
            JSLiteralToken token = tokens.expect(JSTokenType.LITERAL);
            tokens.expect(JSTokenType.END_STRING);
            return new JSLiteralNode(new JSStringObject(token.getValue()));
        } else {
            throw new UnsupportedOperationException(top.toString());
        }
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

    public JSASTNode parseExpression(JSTokenList tokens) throws JSParseException {
        JSASTNode left = parseAdditive(tokens);
        if (tokens.hasAny()) {
            if (tokens.peek().getType() == JSTokenType.EQUALS_SIGN) {
                tokens.expect(JSTokenType.EQUALS_SIGN);
                JSASTNode right = parseAdditive(tokens);
                if (left instanceof JSAccessPropertyNode) {
                    JSASTNode value = ((JSAccessPropertyNode) left).getValue();
                    JSASTNode key = ((JSAccessPropertyNode) left).getKey();
                    return new JSEqualsNode(value, key, right);
                } else {
                    throw new JSParseException("Expected lvalue on left side of `=`");
                }
            }
        }
        return left;
    }

}
