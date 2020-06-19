package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSMap;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

import java.util.HashMap;
import java.util.Map;

public class JSMapNode implements JSASTNode {

    private final Map<String, JSASTNode> nodes;

    public JSMapNode(Map<String, JSASTNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        Map<String, JSObject> map = new HashMap<>();
        for (String s : nodes.keySet()) {
            map.put(s, nodes.get(s).evaluate(context));
        }
        return new JSMap(map);
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("MAP LITERAL (SIZE " + nodes.size() + ")");
        nodes.forEach((key, value) -> {
            logger.println("KEY) " + key);
            logger.println("VALUE)");
            logger.pushprint(value);
        });
    }
}
