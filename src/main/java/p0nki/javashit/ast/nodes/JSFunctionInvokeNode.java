package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSFunction;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

import java.util.List;

public class JSFunctionInvokeNode implements JSASTNode {

    private final JSASTNode function;
    private final List<JSASTNode> arguments;

    public JSFunctionInvokeNode(JSASTNode function, List<JSASTNode> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        JSFunction functionValue = function.evaluate(context).asFunction();
        JSContext newContext = context.push();
        // TODO: `arguments` parameter, add arrays for this
        for (int i = 0; i < functionValue.getArgumentNames().size(); i++) {
            if (i < arguments.size()) {
                newContext.set(functionValue.getArgumentNames().get(i), arguments.get(i).evaluate(context));
            } else {
                newContext.set(functionValue.getArgumentNames().get(i), JSUndefinedObject.INSTANCE);
            }
        }
        return functionValue.getNode().evaluate(newContext);
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("FUNCTION INVOKE");
        logger.println("FUNC)");
        logger.pushprint(function);
        logger.println("ARGUMENTS)");
        logger.push();
        for (int i = 0; i < arguments.size(); i++) {
            logger.println(i + ")");
            logger.pushprint(arguments.get(i));
        }
        logger.pop();
    }

}
