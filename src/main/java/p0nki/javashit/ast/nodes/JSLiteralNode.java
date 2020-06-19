package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;

public class JSLiteralNode implements JSASTNode {

    private final JSObject value;

    public JSLiteralNode(JSObject value) {
        this.value = value;
    }

    @Override
    public JSObject evaluate(JSContext context) {
        return value;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("LITERAL " + value.stringify());
    }

}
