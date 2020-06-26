package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class ThrowNode implements ASTNode {

    private final ASTNode node;

    public ThrowNode(ASTNode node) {
        this.node = node;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        throw new PESLEvalException(node.evaluate(context));
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("THROW");
        logger.pushPrint(node);
    }
}
