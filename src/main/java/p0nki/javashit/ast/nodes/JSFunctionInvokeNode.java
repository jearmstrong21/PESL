package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSArray;
import p0nki.javashit.object.JSFunction;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

import java.util.ArrayList;
import java.util.List;

public class JSFunctionInvokeNode implements JSASTNode {

    private final JSASTNode thisNode;
    private final JSASTNode function;
    private final List<JSASTNode> arguments;

    public JSFunctionInvokeNode(JSASTNode thisNode, JSASTNode function, List<JSASTNode> arguments) {
        this.thisNode = thisNode;
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        JSFunction functionValue = function.evaluate(context).asFunction();
        JSContext newContext = context.push();
        List<JSObject> evaluatedArguments = new ArrayList<>();
        for (JSASTNode argument : arguments) {
            evaluatedArguments.add(argument.evaluate(context));
        }
        for (int i = 0; i < functionValue.getArgumentNames().size(); i++) {
            if (i < arguments.size()) {
                newContext.let(functionValue.getArgumentNames().get(i), evaluatedArguments.get(i));
            } else {
                newContext.let(functionValue.getArgumentNames().get(i), JSUndefinedObject.INSTANCE);
            }
        }
        newContext.set("arguments", new JSArray(evaluatedArguments));
        if (function instanceof JSAccessPropertyNode) {
            JSASTNode obj = ((JSAccessPropertyNode) function).getValue();
            if(obj==null)newContext.setThis(JSUndefinedObject.INSTANCE);
            else newContext.setThis(((JSAccessPropertyNode) function).getValue().evaluate(context));
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
