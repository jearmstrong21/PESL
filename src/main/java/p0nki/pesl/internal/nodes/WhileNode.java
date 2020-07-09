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

public class WhileNode implements ASTNode {

    private final ASTNode condition;
    private final ASTNode body;

    public WhileNode(ASTNode condition, ASTNode body) {
        this.condition = condition;
        this.body = body;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        while (condition.evaluate(context).asBoolean().getValue()) {
            body.evaluate(context.push());
        }
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        condition.validate(TreeRequirement.BOOLEAN);
        body.validate();
        check(requirements, ObjectType.UNDEFINED);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("WHILE");
        logger.println("CONDITION)");
        logger.pushPrint(condition);
        logger.println("BODY)");
        logger.pushPrint(body);
    }
}
