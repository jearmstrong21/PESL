package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;
import p0nki.pesl.internal.token.type.BiOperatorType;

import javax.annotation.Nonnull;
import java.util.Set;

public class OperatorNode implements ASTNode {

    private final ASTNode[] nodes;
    private final BiOperatorType operatorType;

    public OperatorNode(ASTNode[] nodes, BiOperatorType operatorType) {
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
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        for (ASTNode node : nodes) {
            node.validate();
        }
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
