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

import javax.annotation.Nonnull;
import java.util.Set;

public class TryNode implements ASTNode {

    private final ASTNode tryNode;
    private final String excName;
    private final ASTNode catchNode;

    public TryNode(ASTNode tryNode, String excName, ASTNode catchNode) {
        this.tryNode = tryNode;
        this.excName = excName;
        this.catchNode = catchNode;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
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
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        tryNode.validate();
        catchNode.validate();
        check(requirements, ObjectType.UNDEFINED);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("TRY)");
        logger.pushPrint(tryNode);
        logger.println(String.format("CATCH) %s", excName));
        logger.pushPrint(catchNode);
    }
}
