package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.object.ObjectType;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.Set;

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
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        check(requirements, new ObjectType(value));
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("LITERAL " + value.stringify());
    }

}
