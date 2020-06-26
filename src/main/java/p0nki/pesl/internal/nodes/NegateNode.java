package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class NegateNode implements ASTNode {

    private final ASTNode value;

    public NegateNode(ASTNode value) {
        this.value = value;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        return new NumberObject(-value.evaluate(context).asNumber().getValue());
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("NEGATE");
        logger.pushPrint(value);
    }
}
