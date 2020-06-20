package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSNumberObject;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSNegateNode implements JSASTNode {

    private final JSASTNode value;

    public JSNegateNode(JSASTNode value) {
        this.value = value;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        return new JSNumberObject(-value.evaluate(context).asNumber().getValue());
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("NEGATE");
        logger.pushprint(value);
    }
}
