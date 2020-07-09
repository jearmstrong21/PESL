package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.MapObject;
import p0nki.pesl.api.object.ObjectType;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapNode implements ASTNode {

    private final Map<ASTNode, ASTNode> nodes;

    public MapNode(Map<ASTNode, ASTNode> nodes) {
        this.nodes = nodes;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        Map<String, PESLObject> map = new HashMap<>();
        for (Map.Entry<ASTNode, ASTNode> entry : nodes.entrySet()) {
            ASTNode key = entry.getKey();
            ASTNode value = entry.getValue();
            map.put(key.evaluate(context).castToString(), value.evaluate(context));
        }
        return new MapObject(map);
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        for (Map.Entry<ASTNode, ASTNode> entry : nodes.entrySet()) {
            entry.getKey().validate();
            entry.getValue().validate();
        }
        check(requirements, ObjectType.MAP);
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
