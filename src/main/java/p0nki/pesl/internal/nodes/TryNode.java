package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class TryNode implements ASTNode {

    private final ASTNode tryNode;
    private final String excName;
    private final ASTNode catchNode;

    public TryNode(ASTNode tryNode, String excName, ASTNode catchNode) {
        this.tryNode = tryNode;
        this.excName = excName;
        this.catchNode = catchNode;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        try {
            tryNode.evaluate(context.push());
        } catch (PESLEvalException evalException) {
            PESLContext pushed = context.push();
            pushed.let(excName, evalException.getObject());
            catchNode.evaluate(pushed);
        }
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {

    }
}
