package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;
import p0nki.javashit.run.JSMapLike;

public class JSEqualsNode implements JSASTNode {

    private final JSASTNode maplike;
    private final JSASTNode key;
    private final JSASTNode equals;
    private final boolean let;

    public JSEqualsNode(JSASTNode maplike, JSASTNode key, JSASTNode equals, boolean let) {
        this.maplike = maplike;
        this.key = key;
        this.equals = equals;
        this.let = let;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        JSMapLike maplikeValue;
        if (maplike == null) {
            maplikeValue = context;
        } else {
            maplikeValue = maplike.evaluate(context).asMapLike();
        }
        String keyValue = key.evaluate(context).asString().getValue();
        JSObject equalsValue = equals.evaluate(context);
        if (let && maplikeValue instanceof JSContext) {
            ((JSContext) maplikeValue).let(keyValue, equalsValue);
        } else {
            maplikeValue.set(keyValue, equalsValue);
        }
        return equalsValue;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("EQUALS_NODE");
        logger.println("MAPLIKE");
        logger.pushprint(maplike);
        logger.println("KEY");
        logger.pushprint(key);
        logger.println("EQUALS");
        logger.pushprint(equals);
//        System.out.println("--- " + equals);
    }

}
