package p0nki.javashit.object;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.ast.nodes.JSASTNode;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

import java.util.ArrayList;
import java.util.List;

public class JSFunction extends JSObject {

    @FunctionalInterface
    public interface JSFInterface {

        JSObject operate(List<JSObject> arguments) throws JSEvalException;

    }

    public static JSFunction of(String name, JSFInterface jsfInterface) {
        return new JSFunction(new ArrayList<>(), new JSASTNode() {
            @Override
            public JSObject evaluate(JSContext context) throws JSEvalException {
                return jsfInterface.operate(context.get("arguments").asArray().getValues());
            }

            @Override
            public void print(IndentedLogger logger) {
                logger.println("JSFInterface " + name);
            }
        });
    }

    private final List<String> argumentNames;
    private final JSASTNode node;

    public JSFunction(List<String> argumentNames, JSASTNode node) {
        this.argumentNames = argumentNames;
        this.node = node;
    }

    public JSASTNode getNode() {
        return node;
    }

    public List<String> getArgumentNames() {
        return argumentNames;
    }

    //TODO better stringify with argument names
    @Override
    public String stringify() {
        return "function { ... code ... }";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.FUNCTION;
    }

    @Override
    public String castToString() {
        return "function { ... code ... }";
    }
}
