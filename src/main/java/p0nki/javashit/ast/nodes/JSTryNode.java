package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSTryNode implements JSASTNode {

    private final JSASTNode tryNode;
    private final String excName;
    private final JSASTNode catchNode;

    public JSTryNode(JSASTNode tryNode, String excName, JSASTNode catchNode) {
        this.tryNode = tryNode;
        this.excName = excName;
        this.catchNode = catchNode;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        try {
            tryNode.evaluate(context.push());
        } catch (JSEvalException evalException) {
            JSContext pushed = context.push();
            pushed.let(excName, evalException.getObject());
            catchNode.evaluate(pushed);
        }
        return JSUndefinedObject.INSTANCE;
    }

    @Override
    public void print(IndentedLogger logger) {

    }
}
