package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSReturnNode implements JSASTNode {

    private final JSASTNode value;

    public JSReturnNode(JSASTNode value) {
        this.value = value;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        throw new JSInternalReturnException(value.evaluate(context));
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("RETURN");
        logger.pushprint(value);
    }
}
