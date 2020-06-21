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

    private final JSObject thisObject;

    public JSFunction(JSObject thisObject, List<String> argumentNames, JSASTNode node) {
        this.thisObject = thisObject;
        this.argumentNames = argumentNames;
        this.node = node;
    }

    private final List<String> argumentNames;
    private final JSASTNode node;

    public static JSFunction of(JSFInterface jsfInterface) {
        return new JSFunction(null, new ArrayList<>(), new JSASTNode() {
            @Override
            public JSObject evaluate(JSContext context) throws JSEvalException {
                return jsfInterface.operate(context.get("arguments").asArray().getValues());
            }

            @Override
            public void print(IndentedLogger logger) {
                logger.println("[native function]");
            }
        });
    }

    public JSObject getThisObject() {
        return thisObject;
    }

    public JSASTNode getNode() {
        return node;
    }

    public List<String> getArgumentNames() {
        return argumentNames;
    }

    @Override
    public String stringify() {
        return String.format("function(%s)", String.join(", ", argumentNames));
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.FUNCTION;
    }

    @Override
    public String castToString() {
        return stringify();
    }
}
