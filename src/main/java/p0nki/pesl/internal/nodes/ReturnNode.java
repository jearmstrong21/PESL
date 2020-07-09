package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;
import p0nki.pesl.internal.InternalReturnException;

import javax.annotation.Nonnull;
import java.util.Set;

public class ReturnNode implements ASTNode {

    private final ASTNode value;

    public ReturnNode(ASTNode value) {
        this.value = value;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        throw new InternalReturnException(value.evaluate(context));
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        value.validate();
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("RETURN");
        logger.pushPrint(value);
    }
}
