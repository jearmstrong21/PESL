package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;

public class ForEachNode implements ASTNode {

    private final String varName;
    private final String indexName;
    private final ASTNode array;
    private final ASTNode body;

    public ForEachNode(String varName, String indexName, ASTNode array, ASTNode body) {
        this.varName = varName;
        this.indexName = indexName;
        this.array = array;
        this.body = body;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        ArrayObject arrayValue = array.evaluate(context).asArray();
        for (int i = 0; i < arrayValue.getValues().size(); i++) {
            PESLContext pushed = context.push();
            pushed.let(varName, arrayValue.getValues().get(i));
            if (indexName != null) pushed.let(indexName, new NumberObject(i));
            body.evaluate(pushed);
        }
        return UndefinedObject.INSTANCE;
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("FOREACH " + varName);
        logger.println("ARRAY)");
        logger.pushPrint(array);
        logger.println("BODY)");
        logger.pushPrint(body);
    }
}
