package p0nki.javashit;

import org.junit.Test;
import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.ast.JSASTCreator;
import p0nki.javashit.ast.JSParseException;
import p0nki.javashit.ast.nodes.JSASTNode;
import p0nki.javashit.object.JSFunction;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.token.JSTokenList;
import p0nki.javashit.token.JSTokenizeException;
import p0nki.javashit.token.JSTokenizer;

import java.util.ArrayList;
import java.util.HashMap;

public class MainTests {

    private void tokenize(String str) throws JSTokenizeException {
        System.out.println(str);
        JSTokenizer tokenizer = new JSTokenizer();
        JSTokenList tokens = tokenizer.tokenize(str);
        for (int i = 0; i < tokens.getSize(); i++) {
            System.out.println(tokens.get(i));
        }
    }

    private void ast(JSContext ctx, String str) throws JSTokenizeException, JSParseException, JSEvalException {
        JSTokenizer tokenizer = new JSTokenizer();
        JSTokenList tokens = tokenizer.tokenize(str);
        JSASTCreator astCreator = new JSASTCreator();
        JSASTNode node = astCreator.parseExpression(tokens);
//        node.print(new IndentedLogger());
        System.out.println("<- " + str);
        System.out.println("-> " + node.evaluate(ctx));
//        ctx.keys().forEach(key -> {
//            try {
//                System.out.println("KEY " + key + " = " + ctx.get(key).stringify());
//            } catch (JSReferenceException e) {
//                e.printStackTrace();
//            }
//        });
        System.out.println();
    }

    @Test
    public void test() throws JSEvalException, JSTokenizeException, JSParseException {
        JSContext ctx = new JSContext(null, new HashMap<>());
        ctx.set("println", new JSFunction(new ArrayList<String>() {{
            add("x");
        }}, new JSASTNode() {
            @Override
            public JSObject evaluate(JSContext context) throws JSEvalException {
                System.out.println("PRINTLN " + context.get("x"));
                return JSUndefinedObject.INSTANCE;
            }

            @Override
            public void print(IndentedLogger logger) {
                logger.println("PRINTLN FUNCTION");
            }
        }));
//        ast(ctx, "function(x) { println(2+x) } (4)");
        ast(ctx, "add = function(x,y) { return x + y }");
        ast(ctx, "add(5,4)");
        ast(ctx, "i=5");
        ast(ctx, "pure = function( ) { let i = 3 return i }");
        ast(ctx, "pure()");
        ast(ctx, "i");
        ast(ctx, "impure = function(){i=3 return i}");
        ast(ctx, "impure()");
        ast(ctx, "i");

        ast(ctx, "a\nb = 5");
        ast(ctx, "a\nb * a\nb");
        // in order of importance:

        // TODO: arrays
        // TODO: `arguments` keyword in functions

        // TODO: `global` (with a new token type) object which represents the global ctx
        // TODO: try/catch, easy to implement, catch JSEvalException and pass in the stringliteral specified by it into the catch

        // TODO: function binding to `this`

        // TODO: variable shadowing with special keyword? is this too hard to implement?

        // TODO: value listeners which listen for a value change
//        tokenize( "println(5)");
//        ast(ctx, "z = {\"x\": 3, y: 4}");
//        ast(ctx, "z.ten = {test: 10}");
//        ast(ctx, "z[\"ten\"]");
//        ast(ctx, "z[\"t\"+\"en\"]");
//        ast(ctx, "z[\"a key which doesn't exist\"]");
    }

}
