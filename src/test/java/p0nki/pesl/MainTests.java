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
        }
    }

    long curMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
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

    double factorial(double x) {
        if (x < 2) return 1;
        else return x * factorial(x - 1);
    }

    @Test
    public void test() {
        test(() -> {
            PESLContext ctx = new PESLContext(null, new HashMap<>());
            ctx.set("println", PESLBuiltins.PRINTLN);
            ctx.set("dir", PESLBuiltins.DIR);
            ctx.set("Math", PESLBuiltins.MATH);
            ctx.set("Data", PESLBuiltins.DATA);
            ctx.set("System", PESLBuiltins.SYSTEM);

            run(ctx, "f=function(x){if(x<2)return 1 else return x*f(x-1)}f(10)");
        }, () -> {
            System.out.println(factorial(10));
        });
    }

}
