package p0nki.javashit;

import org.junit.Test;
import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.ast.JSASTCreator;
import p0nki.javashit.ast.JSParseException;
import p0nki.javashit.ast.nodes.JSASTNode;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.run.JSReferenceException;
import p0nki.javashit.token.JSTokenList;
import p0nki.javashit.token.JSTokenizeException;
import p0nki.javashit.token.JSTokenizer;

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
        ctx.keys().forEach(key -> {
            try {
                System.out.println("KEY " + key + " = " + ctx.get(key).stringify());
            } catch (JSReferenceException e) {
                e.printStackTrace();
            }
        });
        System.out.println();
    }

    @Test
    public void test() throws JSEvalException, JSTokenizeException, JSParseException {
//        tokenize("4 \" test 32 \" \"45\"");
//        tokenize("4 * (5 + 3 * 42)");
//        ast("2 + 4 * 5");
//        ast("2 + 4 * 5 + 3");
//        ast("x = 4");
//        tokenize("4 xy 2x");
        JSContext ctx = new JSContext(null, new HashMap<>());
        ast(ctx, "z = {\"x\": 3, y: 4}");
        ast(ctx, "z.ten = {test: 10}");
        ast(ctx, "z[\"ten\"]");
        ast(ctx, "z[\"t\"+\"en\"]");
    }

}
