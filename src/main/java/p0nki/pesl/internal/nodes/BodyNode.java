package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ObjectType;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;
import p0nki.pesl.internal.InternalReturnException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class BodyNode implements ASTNode {

    private final List<ASTNode> nodes;
    private final boolean catchReturn;

    public BodyNode(List<ASTNode> nodes, boolean catchReturn) {
        this.nodes = nodes;
        this.catchReturn = catchReturn;
    }

    public List<ASTNode> getNodes() {
        return nodes;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        if (catchReturn) {
            try {
                for (ASTNode node : nodes) {
                    node.evaluate(context);
                }
            } catch (InternalReturnException e) {
                return e.getValue();
            }
        } else {
            for (ASTNode node : nodes) {
                node.evaluate(context);
            }
        }
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        for (ASTNode node : nodes) node.validate();
        check(requirements, ObjectType.UNDEFINED);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("FUNCTION_BODY (" + nodes.size() + ")");
        for (int i = 0; i < nodes.size(); i++) {
            logger.println(i + ")");
            logger.pushPrint(nodes.get(i));
        }
    }
}
