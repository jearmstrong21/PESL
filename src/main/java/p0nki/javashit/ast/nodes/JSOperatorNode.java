package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.token.type.JSOperatorType;

public class JSOperatorNode implements JSASTNode {

    private final JSASTNode[] nodes;
    private final JSOperatorType operatorType;

    public JSOperatorNode(JSASTNode[] nodes, JSOperatorType operatorType) {
        this.nodes = nodes;
        this.operatorType = operatorType;
    }


    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        JSObject value = nodes[0].evaluate(context);
        for (int i = 1; i < nodes.length; i++) {
            value = operatorType.apply(value, nodes[i].evaluate(context));
        }
        return value;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("OPERATOR " + operatorType.toString());
        for (int i = 0; i < nodes.length; i++) {
            logger.println(i + ")");
            logger.pushprint(nodes[i]);
        }
    }
}
