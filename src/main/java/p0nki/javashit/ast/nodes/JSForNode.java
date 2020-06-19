package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSForNode implements JSASTNode {

    private final JSASTNode init;
    private final JSASTNode condition;
    private final JSASTNode increment;
    private final JSASTNode body;

    public JSForNode(JSASTNode init, JSASTNode condition, JSASTNode increment, JSASTNode body) {
        this.init = init;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        init.evaluate(context);
        while (condition.evaluate(context).asBoolean().getValue()) {
            body.evaluate(context.push());
            increment.evaluate(context);
        }
        return JSUndefinedObject.INSTANCE;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("FOR LOOP");
        logger.println("INIT)");
        logger.pushprint(init);
        logger.println("CONDITION)");
        logger.pushprint(condition);
        logger.println("INCREMENT)");
        logger.pushprint(increment);
        logger.println("BODY)");
        logger.pushprint(body);
    }
}
