package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSThisNode implements JSASTNode {

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        return context.getThis();
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("THIS");
    }

}
