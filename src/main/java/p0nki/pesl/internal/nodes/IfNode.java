package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class IfNode implements ASTNode {

    private final ASTNode condition;
    private final ASTNode then;
    private final ASTNode otherwise;

    public IfNode(ASTNode condition, ASTNode then, ASTNode otherwise) {
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        if (condition.evaluate(context).asBoolean().getValue()) {
            then.evaluate(context.push());
        } else if (otherwise != null) {
            otherwise.evaluate(context.push());
        }
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("IF");
        logger.println("CONDITION)");
        logger.pushPrint(condition);
        logger.println("THEN)");
        logger.pushPrint(then);
        logger.println("OTHERWISE)");
        logger.pushPrint(otherwise);
    }
}
