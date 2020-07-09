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
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        condition.validate(TreeRequirement.BOOLEAN);
        then.validate();
        otherwise.validate();
        check(requirements, ObjectType.UNDEFINED);
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
