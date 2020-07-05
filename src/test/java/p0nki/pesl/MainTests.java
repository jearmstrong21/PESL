package p0nki.pesl;

import org.junit.Test;
import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.builtins.PESLBuiltins;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLParseException;
import p0nki.pesl.api.parse.PESLParser;
import p0nki.pesl.api.token.PESLTokenList;
import p0nki.pesl.api.token.PESLTokenizeException;
import p0nki.pesl.api.token.PESLTokenizer;

import java.util.HashMap;

public class MainTests {

    private void run(PESLContext ctx, String str) {
        PESLTokenizer tokenizer = new PESLTokenizer();
        System.out.println();
        System.out.println("<- " + str);
        PESLTokenList tokens;
        try {
            tokens = tokenizer.tokenize(str);
        } catch (PESLTokenizeException e) {
            int index = e.getIndex();
            System.out.print("   ");
            for (int i = 0; i < index; i++) System.out.print(" ");
            System.out.println("^");
            System.out.println("Tokenize error\n" + e.getMessage());
            return;
        }
        PESLParser astCreator = new PESLParser();
        try {
            while (tokens.hasAny()) {
                ASTNode node = astCreator.parseExpression(tokens);
//                node.print(new PESLIndentedLogger());
                PESLObject result = node.evaluate(ctx);
                System.out.println("-> " + result.castToString());
            }
        } catch (PESLParseException e) {
            System.out.print("  ");
            for (int i = 0; i < e.getToken().getStart(); i++) {
                System.out.print(" ");
            }
            for (int i = 0; i < e.getToken().getEnd() - e.getToken().getStart(); i++) {
                System.out.print("^");
            }
            System.out.println();
            System.out.println(" " + e.getToken().toString());
            System.out.println("Parse error\n" + e.getMessage());
            e.printStackTrace();
        } catch (PESLEvalException e) {
            System.out.println("Eval error");
            System.out.println(e.getObject().toString());
//            e.printStackTrace();
        }
    }

    long curMem() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
    }

    private void test(Runnable pesl, Runnable java) {
        long startMem, startTime, endMem, endTime;
        startMem = curMem();
        startTime = System.currentTimeMillis();
        pesl.run();
        endMem = curMem();
        endTime = System.currentTimeMillis();
        System.out.println("PESL: " + (endMem - startMem) + "memory, " + (endTime - startTime) + "ms");
        startMem = curMem();
        startTime = System.currentTimeMillis();
        java.run();
        endMem = curMem();
        endTime = System.currentTimeMillis();
        System.out.println("JAVA: " + (endMem - startMem) + "memory, " + (endTime - startTime) + "ms");
    }

    double fib(double x) {
        if (x < 1) return 0;
        else if (x < 2) return 1;
        else return fib(x - 1) + fib(x - 2);
    }

    @Test
    public void test() {
        PESLContext ctx = new PESLContext(null, new HashMap<>());
        ctx.setKey("println", PESLBuiltins.PRINTLN);
        ctx.setKey("dir", PESLBuiltins.DIR);
        ctx.setKey("Math", PESLBuiltins.MATH);
        ctx.setKey("Data", PESLBuiltins.DATA);
        ctx.setKey("System", PESLBuiltins.SYSTEM);

        run(ctx, "x = {a: 4, ab: 5}");
        run(ctx, "x.a");
        run(ctx, "x.ab");
        run(ctx, "x[\"a\"+\"b\"]");
        run(ctx, "x[\"12ab5\".substring(2,4)]");
        run(ctx, "x.a = x.a + 1");
        run(ctx, "x.a");
        run(ctx, "x[\"5\"] = 10");
        run(ctx, "x");
        run(ctx, "x[\"5.2\"]");
        run(ctx, "x[5]");
        run(ctx, "x[\"5\"]");
        run(ctx, "y = [4, 5, {i: 10, j: 2}, [6, 9]]");
        run(ctx, "z = y");
        run(ctx, "w = Data.copy(y)");
        run(ctx, "y[2].i=y[2].j+5");
        run(ctx, "y");
        run(ctx, "z");
        run(ctx, "w");
    }

}
