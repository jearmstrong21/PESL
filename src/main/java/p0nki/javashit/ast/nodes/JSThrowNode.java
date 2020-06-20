package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSThrowNode implements JSASTNode {

    private final JSASTNode node;

    public JSThrowNode(JSASTNode node) {
        this.node = node;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        throw new JSEvalException(node.evaluate(context));
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("THROW");
        logger.pushprint(node);
    }
}
