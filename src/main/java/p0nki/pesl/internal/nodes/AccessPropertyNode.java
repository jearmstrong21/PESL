package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

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

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        if (value == null) return context.get(key.evaluate(context).asString().getValue());
        return value.evaluate(context).asMapLike().get(key.evaluate(context).castToString());
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("ACCESS_PROPERTY");
        logger.println("VALUE)");
        logger.pushPrint(value);
        logger.println("KEY)");
        logger.pushPrint(key);
    }

}
