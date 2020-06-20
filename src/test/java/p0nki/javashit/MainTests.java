package p0nki.javashit;

import org.junit.Test;
import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.ast.JSASTCreator;
import p0nki.javashit.ast.JSParseException;
import p0nki.javashit.ast.nodes.JSASTNode;
import p0nki.javashit.object.*;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.token.JSTokenList;
import p0nki.javashit.token.JSTokenizeException;
import p0nki.javashit.token.JSTokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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
                logger.println("PRINTLN");
            }
        }));
        ctx.set("Math", new JSMap(new HashMap<>())
                .builderSet("random", JSFunction.of("MATH::random", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 0);
                    return new JSNumberObject(Math.random());
                }))
                .builderSet("sqrt", JSFunction.of("MATH::sqrt", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.sqrt(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("floor", JSFunction.of("MATH::floor", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.floor(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("ceil", JSFunction.of("MATH::ceil", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.ceil(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("pow", JSFunction.of("MATH::pow", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 2);
                    return new JSNumberObject(Math.pow(arguments.get(0).asNumber().getValue(), arguments.get(1).asNumber().getValue()));
                }))
                .builderSet("abs", JSFunction.of("MATH::abs", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.abs(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("sin", JSFunction.of("MATH::sin", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.sin(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("cos", JSFunction.of("MATH::cos", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.cos(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("tan", JSFunction.of("MATH::tan", arguments -> {
                    JSEvalException.validArgumentListLength(arguments, 1);
                    return new JSNumberObject(Math.tan(arguments.get(0).asNumber().getValue()));
                }))
                .builderSet("min", JSFunction.of("MATH::min", arguments -> {
                    if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                    if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                    double min = arguments.get(0).asNumber().getValue();
                    for (int i = 1; i < arguments.size(); i++) {
                        min = Math.min(min, arguments.get(i).asNumber().getValue());
                    }
                    return new JSNumberObject(min);
                }))
                .builderSet("max", JSFunction.of("MATH::max", arguments -> {
                    if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                    if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                    double max = arguments.get(0).asNumber().getValue();
                    for (int i = 1; i < arguments.size(); i++) {
                        max = Math.max(max, arguments.get(i).asNumber().getValue());
                    }
                    return new JSNumberObject(max);
                }))
                .builderSet("any", JSFunction.of("MATH::any", arguments -> {
                    if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                    if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                    boolean value = arguments.get(0).asBoolean().getValue();
                    for (int i = 1; i < arguments.size() && !value; i++) {
                        value = arguments.get(i).asBoolean().getValue();
                    }
                    return new JSBooleanObject(value);
                }))
                .builderSet("all", JSFunction.of("MATH::all", arguments -> {
                    if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                    if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                    boolean value = arguments.get(0).asBoolean().getValue();
                    for (int i = 1; i < arguments.size() && value; i++) {
                        value = arguments.get(i).asBoolean().getValue();
                    }
                    return new JSBooleanObject(value);
                }))
        );
        ctx.set("dir", JSFunction.of("dir", arguments -> {
            JSEvalException.validArgumentListLength(arguments, 1);
            return new JSArray(arguments.get(0).asMapLike().keys().stream().map(JSStringObject::new).collect(Collectors.toList()));
        }));

//        ast(ctx, "Math");
//        ast(ctx, "Math.min(5, 10, -3)");
//        ast(ctx, "!true ^ false");
//        ast(ctx, "!false ^ false");
//        ast(ctx, "!(false & false)");
//        ast(ctx, "!false & true");
//        ast(ctx, "5 +- 3 +- 4");

//        ast(ctx, "function(x) { println(2+x) } (4)");
//        ast(ctx, "add = function(x,y) { return x + y }");
//        ast(ctx, "add(5,4)");

        ast(ctx, "Math.any(true, false)");
        ast(ctx, "Math.any(false, false)");
        ast(ctx, "Math.all(true, false)");
        ast(ctx, "Math.all(true, true)");

        ast(ctx, "Math.any([false, true])");
        ast(ctx, "Math.min([3, 4, 5])");

//        ast(ctx, "i=5");
//        ast(ctx, "pure = function( ) { let i = 3 return i }");
//        ast(ctx, "pure()");
//        ast(ctx, "i");
//        ast(ctx, "impure = function(){i=3 return i}");
//        ast(ctx, "impure()");
//        ast(ctx, "i");
//        ast(ctx, "my_object = {func: function(a) { return 3 + a * 5}}");
//        ast(ctx, "my_object.func(5)");

//        ast(ctx, "Math");
//        ast(ctx, "f = function() {println(arguments)}");
//        ast(ctx, "f(3, 4)");
//        ast(ctx, "f()");

//        ast(ctx, "a\nb = 5");
//        ast(ctx, "a\nb * a\nb");

//        ast(ctx, "5 < 4");
//        ast(ctx, "5 < 4 ^ 4 < 5");
//        ast(ctx, "4 < 5");
//        ast(ctx, "x = 4 < 5");
//        ast(ctx, "y = 5 < 4");
//        ast(ctx, "x");
//        ast(ctx, "y");
//        ast(ctx, "x & y");
//        ast(ctx, "return 5");

//        ast(ctx, "i = 5");
//        ast(ctx, "i=i+1");
//        ast(ctx, "i");

//        ast(ctx, "dir(\"test\")");
//        ast(ctx, "dir([5, 4])");
//        ast(ctx, "dir({x: 5, y: 4})");
//        ast(ctx, "dir(dir)");

//        ast(ctx, "z = {increment: function() { this.x = this.x + 1 }, x: 4}");
//        ast(ctx, "z");
//        ast(ctx, "z.increment()");
//        ast(ctx, "z");

//        ast(ctx, "for(i=1;i<5;i=i+1){println(i)}");
//        ast(ctx, "if(5 > 4) { println(true) }");

//        ast(ctx, "factorial = function(x) { if(x>1){return x*factorial(x-1)}else{return 1}}");
//        ast(ctx, "for(let i=1;i<11;i=i+1){println(factorial(i))}");

//        ast(ctx, "throw { x : 5 }");

//        ast(ctx, "i = 5");
//        ast(ctx, "f = function(i) { }");
//        ast(ctx, "f(10)");
//        ast(ctx, "i");

//        ast(ctx, "try { throw Math.random() } catch(exc) { println(exc) }");
//        ast(ctx, "i = 1");
//        ast(ctx, "try { throw Math.random() } catch (i) { i = 5 println(i) }");
//        ast(ctx, "i");
//        ast(ctx, "try { throw 5 } catch (exc) { println(\"ISSUE \" + exc) }");

//        ast(ctx, "f = function(x){return function(y){return x+y}}");
//        ast(ctx, "f");
//        ast(ctx, "f(3)");
//        ast(ctx, "z=f(3)");
//        ast(ctx, "z(5)");

//        ast(ctx, "my_class = function(initCounter) { return { counter:initCounter, increment: function() {this.counter=this.counter+1}, decrement: function() {this.counter=this.counter-1}, get: function(){return this.counter}, set: function(newValue){this.counter=newValue}}}");
//        ast(ctx, "my_class(5)");
//        ast(ctx, "obj = my_class(5)");
//        ast(ctx, "obj.increment()");
//        ast(ctx, "obj.get()");
//        ast(ctx, "obj.set(10)");
//        ast(ctx, "obj.get()");

//        ast(ctx, "arr = [5, 4, {x: 1, y: 2}]");
//        ast(ctx, "foreach(value:arr){println(value)}");
//        ast(ctx, "foreach(value,index:arr){println(index+\" \"+value)}");

        // TODO: `global` (with a new token type) object which represents the global ctx
        // TODO: value listeners which listen for a value change
//        ast(ctx, "z = {\"x\": 3, y: 4}");
//        ast(ctx, "z.ten = {test: 10}");
//        ast(ctx, "z[\"ten\"]");
//        ast(ctx, "z[\"t\"+\"en\"]");
//        ast(ctx, "z[\"a key which doesn't exist\"]");
    }

}
