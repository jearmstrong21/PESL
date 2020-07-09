package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.*;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.Set;

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

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
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
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        array.validate(TreeRequirement.ARRAYLIKE);
        body.validate();
        check(requirements, ObjectType.UNDEFINED);
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
