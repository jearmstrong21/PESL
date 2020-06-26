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

    private void tokenize(String str) throws PESLTokenizeException {
        System.out.println(str);
        PESLTokenizer tokenizer = new PESLTokenizer();
        PESLTokenList tokens = tokenizer.tokenize(str);
        for (int i = 0; i < tokens.getSize(); i++) {
            System.out.println(tokens.get(i));
        }
    }

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
        } catch (PESLEvalException e) {
            System.out.println("Eval error");
            System.out.println(e.getObject().toString());
        }
    }

    @Test
    public void test() {
        PESLContext ctx = new PESLContext(null, new HashMap<>());
        ctx.set("println", PESLBuiltins.PRINTLN);
        ctx.set("dir", PESLBuiltins.DIR);
        ctx.set("Math", PESLBuiltins.MATH);
        ctx.set("Data", PESLBuiltins.DATA);
        ctx.set("System", PESLBuiltins.SYSTEM);

//        run(ctx, "res = Data.write({x: [5, 2], y: 4 * 3})");
//        run(ctx, "Data.read(res)");
//        run(ctx, "Data.read(function(y){return y+2})");
//        run(ctx, "Data.write(function(y){return y+2})");

//        run(ctx, "fib = function(x) { if(x<1){return 0}if(x<2){return 1}return fib(x-1)+fib(x-2)}");
//        run(ctx, "startTime = System.time()");
//        run(ctx, "for(i=0;i<25;i=i+1){fib(i)}");
//        run(ctx, "endTime = System.time()");
//        run(ctx, "println(\"Time in milliseconds: \" + (endTime-startTime))");

        run(ctx, "fib = function(x){if(x<1)return 0 else if(x<2)return 1 else return fib(x-1)+fib(x-2)}");
        run(ctx, "fib(10)");

        run(ctx, "UNICODE = [\"\uD83D\uDCA5\", \"0️⃣\", \"1️⃣\", \"2️⃣\", \"3️⃣\", \"4️⃣\", \"5️⃣\", \"6️⃣\", \"7️⃣\", \"8️⃣\", \"9️⃣\", \"\uD83D\uDD1F\"]\n" +
                "\n" +
                "width = 10\n" +
                "height = 10\n" +
                "pointCount = 20\n" +
                "\n" +
                "points = []\n" +
                "\n" +
                "for (i = 0; i < pointCount; i = i + 1){\n" +
                "  points.push ([Math.floor(width * Math.random()), Math.floor(height * Math.random())])\n" +
                "}\n" +
                "\n" +
                "grid = []\n" +
                "\n" +
                "for (i = 0; i <width; i = i + 1){\n" +
                "  arr=[]\n" +
                "  for (j = 0; j < height; j = j + 1){\n" +
                "    arr.push (1)\n" +
                "  }\n" +
                "  grid.push (arr)\n" +
                "}\n" +
                "\n" +
                "inc = function (x, y) {\n" +
                "  if(x < 0) return undefined\n" +
                "  if(y < 0) return undefined\n" +
                "  if(x > width - 1) return undefined\n" +
                "  if(y > height - 1) return undefined\n" +
                "  grid[x][y] = grid[x][y] + 1\n" +
                "}\n" +
                "\n" +
                "foreach (point : points){\n" +
                "  inc (point[0] - 1, point[1] - 1)\n" +
                "  inc (point[0] - 1, point[1])\n" +
                "  inc (point[0] - 1, point[1] + 1)\n" +
                "  inc (point[0], point[1] - 1)\n" +
                "  inc (point[0], point[1] + 1)\n" +
                "  inc (point[0] + 1, point[1] - 1)\n" +
                "  inc (point[0] + 1, point[1])\n" +
                "  inc (point[0] + 1, point[1] + 1)\n" +
                "}\n" +
                "\n" +
                "foreach (point : points){\n" +
                "  grid[point[0]][point[1]] = 0\n" +
                "}\n" +
                "\n" +
                "formatRow = function (row) {\n" +
                "  formatted = \"\"\n" +
                "  foreach (number : row) {\n" +
                "    formatted = formatted + \"||\" + UNICODE[number] + \"||\"\n" +
                "  }\n" +
                "  return formatted\n" +
                "}\n" +
                "result = \"\"\n" +
                "foreach (row : grid) {\n" +
                "  result = result + formatRow(row) + \"\n" +
                "\"\n" +
                "}\n" +
                "result");

        //TODO: "string literal".substring(3,5) doesn't work, why?

//        run(ctx, "obj = {i:10,get:function(self){return self.i}}");
//        run(ctx, "obj");
//        run(ctx, "obj.get()");
//        run(ctx, "obj2 = {i:40}");
//        run(ctx, "obj2.get=obj.get");
//        run(ctx, "obj2");
//        run(ctx, "obj2.get()");

//        run(ctx, "obj=[3]");
//        run(ctx, "obj[0]");
//        run(ctx, "f = function(input) { input[0] = 5 }");
//        run(ctx, "f(obj)");
//        run(ctx, "obj[0]");

//        run(ctx, "factorial=function(i){if(i<2){return 1}else{return i*factorial(i-1)}}");
//        run(ctx, "factorial(6)");

    }

}
