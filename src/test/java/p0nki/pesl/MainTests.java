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

public class MainTests {

    private PESLObject run(PESLContext ctx, String str) throws PESLTokenizeException, PESLEvalException, PESLParseException {
        PESLTokenizer tokenizer = new PESLTokenizer();
        System.out.println();
        System.out.println("<- " + str);
        PESLTokenList tokens;
        try {
            tokens = tokenizer.tokenize(str);
//            for (int i = 0; i < tokens.getSize(); i++) {
//                System.out.println(tokens.get(i));
//            }
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
        ctx.setKey("println", PESLBuiltins.PRINTLN);

//        run(ctx, "value = [1, 2, 3, 4, 5]");
//        run(ctx, "[println(x) for x in value]");
//        run(ctx, "[x+y for x in [\"a\", \"b\", \"c\"] for y in [1, 2, 3]]");

//        run(ctx, "[x + \" \" + y + \"-\" + z + w for x, y in [\"2\", \"1\", \"0\"] for z: w in {\"a\": \"5\", \"b\": \"3\", \"c\": \"4\"}]");
//        run(ctx, "{x + \"+\" + y: x + y for x in [\"0\", \"1\", \"2\"] for y in [\"0\", \"1\", \"2\"]}");

//        run(ctx, "x = 5");
//        run(ctx, "x *= 4");
//        run(ctx, "x -= 3");
//        run(ctx, "x");
//
//        run(ctx, "4 * 5 >= Math.sqrt(400)");
//        run(ctx, "4 * 5 >= Math.sqrt(400) + 1");

        run(ctx, "x = {}");
        run(ctx, "x");

//        run(ctx, "parseNumber(5)");
//        run(ctx, "parseNumber(\"5\"+\"3\")");
//        run(ctx, "5 is 3 + 4");
//        run(ctx, "4 + 3 is 9 - 2");
//        run(ctx, "4 == 5");
//        run(ctx, "4 <= 5");
//        run(ctx, "4 >= 5");
//        run(ctx, "while(Math.random()<=0.5){println(\"hello world\")}");
//        run(ctx, "5.4.0 + 3.2");
//        run(ctx, "count=0 total=1000 chance=900 for(i=0;i<total;i=i+1){if(Math.random()<chance/total){count=count+1}}count");
//        run(ctx, "count2=0 for(i=0;i<total;i=i+1){if(count2=count2+Math.random()}count2");
//        run(ctx, "x = [2, 3]");
//        run(ctx, "delete x[0]");
//        run(ctx, "x");
//        run(ctx, "x.add({a:4})");
//        run(ctx, "x.add({b:5})");
//        run(ctx, "x");
//        run(ctx, "x.add(1, 5)");
//        run(ctx, "x");
//        run(ctx, "x.add(4, 69)");
//        run(ctx, "x");
//        run(ctx, "5.0+2.3");
//        run(ctx, "x=2");
//        run(ctx, "x");
//        run(ctx, "x += 4");
//        run(ctx, "x");
//        run(ctx, "y=20");
//        run(ctx, "x = y += 4");
//        run(ctx, "x");
//        run(ctx, "y");
//        run(ctx, "x==y");
//        run(ctx, "4<5");
//        run(ctx, "4<=5");
//        run(ctx, "4<=5+1");
//        run(ctx, "x=0");
//        run(ctx, "x=x+1");
//        run(ctx, "x");
//        run(ctx, "for(i=1;i<=10;i=i+1){x=x+1 println(i+\": \"+x)}");
//        run(ctx, "x");
//        run(ctx, "i");
//        run(ctx, "Math.random()<=0.5");
//        run(ctx, "Math.random()<=0.5)");
//        run(ctx, "Math.random()<=0.5");
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
