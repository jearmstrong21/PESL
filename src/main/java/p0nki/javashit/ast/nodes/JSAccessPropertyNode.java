package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSAccessPropertyNode implements JSASTNode {

    private final JSASTNode value;
    private final JSASTNode key;

    public JSAccessPropertyNode(JSASTNode value, JSASTNode key) {
        this.value = value;
        this.key = key;
    }

    public JSASTNode getValue() {
        return value;
    }

    public JSASTNode getKey() {
        return key;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        if (value == null) return context.get(key.evaluate(context).asString().getValue());
        return value.evaluate(context).asMapLike().get(key.evaluate(context).asString().getValue());
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("ACCESS_PROPERTY");
        logger.println("VALUE)");
        logger.pushprint(value);
        logger.println("KEY)");
        logger.pushprint(key);
    }

}
