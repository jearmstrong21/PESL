package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSBooleanObject;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSNotNode implements JSASTNode {

    private final JSASTNode value;

    public JSNotNode(JSASTNode value) {
        this.value = value;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        return new JSBooleanObject(!value.evaluate(context).asBoolean().getValue());
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("NOT");
        logger.pushprint(value);
    }
}
