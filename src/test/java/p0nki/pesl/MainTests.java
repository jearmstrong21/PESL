package p0nki.pesl;

import org.junit.Test;
import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLParseException;
import p0nki.pesl.api.parse.PESLParser;
import p0nki.pesl.api.token.PESLTokenList;
import p0nki.pesl.api.token.PESLTokenizeException;
import p0nki.pesl.api.token.PESLTokenizer;

public class MainTests {

    private PESLObject run(PESLContext ctx, String str) throws PESLTokenizeException, PESLEvalException, PESLParseException {
        PESLTokenizer tokenizer = new PESLTokenizer();
        System.out.println();
        System.out.println("<- " + str);
        PESLTokenList tokens;
        try {
            tokens = tokenizer.tokenize(str);
            for (int i = 0; i < tokens.getSize(); i++) {
                System.out.println(tokens.get(i));
            }
        } catch (PESLTokenizeException e) {
            int index = e.getIndex();
            System.out.print("   ");
            for (int i = 0; i < index; i++) System.out.print(" ");
            System.out.println("^");
            System.out.println("Tokenize error\n" + e.getMessage());
            throw e;
        }
        PESLParser astCreator = new PESLParser();
        try {
            PESLObject lastObject = null;
            while (tokens.hasAny()) {
                ASTNode node = astCreator.parseExpression(tokens);
                lastObject = node.evaluate(ctx);
                System.out.println("-> " + lastObject.castToString());
            }
            return lastObject;
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
            throw e;
        } catch (PESLEvalException e) {
            System.out.println("Eval error");
            System.out.println(e.getObject().toString());
            throw e;
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
    public void test() throws PESLTokenizeException, PESLEvalException, PESLParseException {
        PESLContext ctx = new PESLContext();
//        run(ctx, "parseNumber(5)");
//        run(ctx, "parseNumber(\"5\"+\"3\")");
//        run(ctx, "5 is 3 + 4");
//        run(ctx, "4 + 3 is 9 - 2");
//        run(ctx, "4 == 5");
//        run(ctx, "4 <= 5");
//        run(ctx, "4 >= 5");
//        run(ctx, "while(Math.random()<=0.5){println(\"hello world\")}");
//        run(ctx, "5.4.0 + 3.2");
//        run(ctx, "while(Math.random () <= 0.5){ println (\"hello world\") }");
        run(ctx, "Math.random()<=0.5");
        run(ctx, "Math.random()<=0.5)");
//        run(ctx, "Math.random()<=0.5");
        // TODO: delete "is" token and replace with "EQUALS_BOOL_OP" or something to represent double tokens. once double tokens work do that for <= and >= and == and !=
//        run(ctx, "x = {a: 4, ab: 5}");
//        run(ctx, "x.a");
//        run(ctx, "x.ab");
//        run(ctx, "x[\"a\"+\"b\"]");
//        run(ctx, "x[\"12ab5\".substring(2,4)]");
//        run(ctx, "x.a = x.a + 1");
//        run(ctx, "x.a");
//        run(ctx, "x[\"5\"] = 10");
//        run(ctx, "x");
//        run(ctx, "x[\"5.2\"]");
//        run(ctx, "x[5]");
//        run(ctx, "x[\"5\"]");
//        run(ctx, "y = [4, 5, {i: 10, j: 2}, [6, 9]]");
//        run(ctx, "z = y");
//        run(ctx, "w = Data.copy(y)");
//        run(ctx, "y[2].i=y[2].j+5");
//        run(ctx, "y");
//        run(ctx, "z");
//        run(ctx, "w");
//        run(ctx, "delete 5");
//        PESLObject object = run(ctx, "{a: 4, b: 6}");

//        run(ctx, "x = {a: 4, b: 6}");
//        run(ctx, "delete {a:4,b:6}.a");
//        run(ctx, "delete x.b");
//        run(ctx, "x");
//        run(ctx, "y = [3, 4]");
//        run(ctx, "delete y[1]");
//        run(ctx, "y");
//        run(ctx, "z={a:[5,{b:10}],c:1}");
//        run(ctx, "delete z.a[1].b");
//        run(ctx, "z");
//
//        System.out.println("\n\n\n\n");
//
//        run(ctx, "x = [2, 3]");
//        run(ctx, "x");
//        run(ctx, "x[0] = 5");
//        run(ctx, "x");
//
//        run(ctx, "abcd");
    }

}
