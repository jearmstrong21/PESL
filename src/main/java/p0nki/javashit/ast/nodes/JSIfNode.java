package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSIfNode implements JSASTNode {

    private final JSASTNode condition;
    private final JSASTNode then;
    private final JSASTNode otherwise;

    public JSIfNode(JSASTNode condition, JSASTNode then, JSASTNode otherwise) {
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        if (condition.evaluate(context).asBoolean().getValue()) {
            then.evaluate(context.push());
        } else if (otherwise != null) {
            otherwise.evaluate(context.push());
        }
        return JSUndefinedObject.INSTANCE;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("IF");
        logger.println("CONDITION)");
        logger.pushprint(condition);
        logger.println("THEN)");
        logger.pushprint(then);
        logger.println("OTHERWISE)");
        logger.pushprint(otherwise);
    }
}
