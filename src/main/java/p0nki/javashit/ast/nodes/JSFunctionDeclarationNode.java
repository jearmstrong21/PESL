package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSFunction;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;

import java.util.List;

public class JSFunctionDeclarationNode implements JSASTNode {

    private final List<String> argumentNames;
    private final JSASTNode body;

    public JSFunctionDeclarationNode(List<String> argumentNames, JSASTNode body) {
        this.argumentNames = argumentNames;
        this.body = body;
    }

    @Override
    public JSObject evaluate(JSContext context) {
        return new JSFunction(null, argumentNames, body);
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("FUNC DECLARE");
        logger.println("ARGNAMES (" + String.join(", ", argumentNames) + ")");
        logger.println("BODY)");
        logger.pushprint(body);
    }
}
