package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ArrayNode implements ASTNode {

    private final List<ASTNode> nodes;

    public ArrayNode(List<ASTNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        List<PESLObject> values = new ArrayList<>();
        for (ASTNode node : nodes) {
            values.add(node.evaluate(context));
        }
        return new ArrayObject(values);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("ARRAY LITERAL (SIZE " + nodes.size() + ")");
        for (int i = 0; i < nodes.size(); i++) {
            logger.println(i + ")");
            logger.pushPrint(nodes.get(i));
        }
    }
}
