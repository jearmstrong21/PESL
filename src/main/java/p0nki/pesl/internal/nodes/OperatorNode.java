package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.internal.token.type.OperatorType;

import javax.annotation.Nonnull;

public class OperatorNode implements ASTNode {

    private final ASTNode[] nodes;
    private final OperatorType operatorType;

    public OperatorNode(ASTNode[] nodes, OperatorType operatorType) {
        this.nodes = nodes;
        this.operatorType = operatorType;
    }


    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        PESLObject value = nodes[0].evaluate(context);
        for (int i = 1; i < nodes.length; i++) {
            value = operatorType.apply(value, nodes[i].evaluate(context));
        }
        return value;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("OPERATOR " + operatorType.toString());
        for (int i = 0; i < nodes.length; i++) {
            logger.println(i + ")");
            logger.pushPrint(nodes[i]);
        }
    }
}
