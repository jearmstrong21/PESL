package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.MapObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MapNode implements ASTNode {

    private final Map<String, ASTNode> nodes;

    public MapNode(Map<String, ASTNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        Map<String, PESLObject> map = new HashMap<>();
        for (String s : nodes.keySet()) {
            map.put(s, nodes.get(s).evaluate(context));
        }
        return new MapObject(map);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("MAP LITERAL (SIZE " + nodes.size() + ")");
        nodes.forEach((key, value) -> {
            logger.println("KEY) " + key);
            logger.println("VALUE)");
            logger.pushPrint(value);
        });
    }
}
