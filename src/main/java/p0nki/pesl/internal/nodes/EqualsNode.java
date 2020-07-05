package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.MapLikeObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class EqualsNode implements ASTNode {

    private final ASTNode maplike;
    private final ASTNode key;
    private final ASTNode equals;
    private final boolean let;

    public EqualsNode(ASTNode maplike, ASTNode key, ASTNode equals, boolean let) {
        this.maplike = maplike;
        this.key = key;
        this.equals = equals;
        this.let = let;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        MapLikeObject maplikeValue;
        if (maplike == null) {
            maplikeValue = context;
        } else {
            maplikeValue = maplike.evaluate(context).asMapLike();
        }
        String keyValue = key.evaluate(context).castToString();
        PESLObject equalsValue = equals.evaluate(context);
        if (let && maplikeValue instanceof PESLContext) {
            ((PESLContext) maplikeValue).let(keyValue, equalsValue);
        } else {
            if (let) throw new PESLEvalException("Unexpected let");
            maplikeValue.setKey(keyValue, equalsValue);
        }
        return equalsValue;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("EQUALS_NODE");
        logger.println("MAPLIKE");
        logger.pushPrint(maplike);
        logger.println("KEY");
        logger.pushPrint(key);
        logger.println("EQUALS");
        logger.pushPrint(equals);
//        System.out.println("--- " + equals);
    }

}
