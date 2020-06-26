package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class ForNode implements ASTNode {

    private final ASTNode init;
    private final ASTNode condition;
    private final ASTNode increment;
    private final ASTNode body;

    public ForNode(ASTNode init, ASTNode condition, ASTNode increment, ASTNode body) {
        this.init = init;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        init.evaluate(context);
        while (condition.evaluate(context).asBoolean().getValue()) {
            body.evaluate(context.push());
            increment.evaluate(context);
        }
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("FOR LOOP");
        logger.println("INIT)");
        logger.pushPrint(init);
        logger.println("CONDITION)");
        logger.pushPrint(condition);
        logger.println("INCREMENT)");
        logger.pushPrint(increment);
        logger.println("BODY)");
        logger.pushPrint(body);
    }
}
