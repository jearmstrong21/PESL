package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.Set;

public class ThrowNode implements ASTNode {

    private final ASTNode node;

    public ThrowNode(ASTNode node) {
        this.node = node;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        throw new PESLEvalException(node.evaluate(context));
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        node.validate();
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("THROW");
        logger.pushPrint(node);
    }
}
