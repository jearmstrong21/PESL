package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSArray;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

import java.util.ArrayList;
import java.util.List;

public class JSArrayNode implements JSASTNode {

    private final List<JSASTNode> nodes;

    public JSArrayNode(List<JSASTNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        List<JSObject> values = new ArrayList<>();
        for (JSASTNode node : nodes) {
            values.add(node.evaluate(context));
        }
        return new JSArray(values);
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("ARRAY LITERAL (SIZE " + nodes.size() + ")");
        for (int i = 0; i < nodes.size(); i++) {
            logger.println(i + ")");
            logger.pushprint(nodes.get(i));
        }
    }
}
