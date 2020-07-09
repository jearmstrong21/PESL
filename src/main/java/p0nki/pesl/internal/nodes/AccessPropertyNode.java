package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayLikeObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.Set;

public class AccessPropertyNode implements ASTNode {

    private final ASTNode value;
    private final ASTNode key;

    public AccessPropertyNode(ASTNode value, ASTNode key) {
        this.value = value;
        this.key = key;
    }

    public ASTNode getValue() {
        return value;
    }

    public ASTNode getKey() {
        return key;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        PESLObject keyValue = key.evaluate(context);
        if (value == null) {
            return context.getKey(keyValue.castToString());
        }
        PESLObject valueValue = value.evaluate(context);
        if (keyValue instanceof NumberObject && valueValue instanceof ArrayLikeObject) {
            return ((ArrayLikeObject) valueValue).getElement((int) ((NumberObject) keyValue).getValue());
        }
        return valueValue.asMapLike().getKey(keyValue.castToString());
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("ACCESS_PROPERTY");
        logger.println("VALUE)");
        logger.pushPrint(value);
        logger.println("KEY)");
        logger.pushPrint(key);
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        if (value != null) value.validate(TreeRequirement.MAPLIKE);
    }

}
