package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

import java.util.List;

public class JSBodyNode implements JSASTNode {

    private final List<JSASTNode> nodes;
    private final boolean catchReturn;

    public JSBodyNode(List<JSASTNode> nodes, boolean catchReturn) {
        this.nodes = nodes;
        this.catchReturn = catchReturn;
    }

    public List<JSASTNode> getNodes() {
        return nodes;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        if (catchReturn) {
            try {
                for (JSASTNode node : nodes) {
                    node.evaluate(context);
                }
            } catch (JSInternalReturnException e) {
                return e.getValue();
            }
        }else {
            for (JSASTNode node : nodes) {
                node.evaluate(context);
            }
        }
        return JSUndefinedObject.INSTANCE;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("FUNCTION_BODY (" + nodes.size() + ")");
        for (int i = 0; i < nodes.size(); i++) {
            logger.println(i + ")");
            logger.pushprint(nodes.get(i));
        }
    }
}
