package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayLikeObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class EqualsNode implements ASTNode {

    private final ASTNode value;
    private final ASTNode equals;
    private final boolean let;

    public EqualsNode(ASTNode value, ASTNode equals, boolean let) {
        this.value = value;
        this.equals = equals;
        this.let = let;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
//        if (let && value != null) throw new PESLEvalException("Cannot let on an object, only on a base variable");
        PESLObject equalsValue = equals.evaluate(context);
        if (value instanceof AccessPropertyNode) {
            ASTNode holder = ((AccessPropertyNode) value).getValue();
            ASTNode key = ((AccessPropertyNode) value).getKey();
            PESLObject keyValue = key.evaluate(context);
            if (holder == null) {
                if (let) {
                    context.let(keyValue.castToString(), equalsValue);
                } else {
                    context.setKey(keyValue.castToString(), equalsValue);
                }
            } else {
                if (let) throw new PESLEvalException("Cannot let on an object, only on a base variable");
                PESLObject holderValue = holder.evaluate(context);
                if (holderValue instanceof ArrayLikeObject && keyValue instanceof NumberObject) {
                    ((ArrayLikeObject) holderValue).setElement((int) ((NumberObject) keyValue).getValue(), equalsValue);
                    return equalsValue;
                }
                holderValue.asMapLike().setKey(keyValue.castToString(), equalsValue);
            }
            return equalsValue;
        }
        throw new PESLEvalException("Cannot assign value to non property access");
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("EQUALS_NODE");
        logger.println("VALUE");
        logger.pushPrint(value);
        logger.println("EQUALS");
        logger.pushPrint(equals);
//        System.out.println("--- " + equals);
    }

}
