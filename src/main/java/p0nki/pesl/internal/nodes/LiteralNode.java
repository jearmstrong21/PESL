package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class LiteralNode implements ASTNode {

    private final PESLObject value;

    public LiteralNode(PESLObject value) {
        this.value = value;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) {
        return value;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("LITERAL " + value.stringify());
    }

}
