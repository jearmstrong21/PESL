package p0nki.javashit.ast.nodes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSArray;
import p0nki.javashit.object.JSNumberObject;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.object.JSUndefinedObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public class JSForEachNode implements JSASTNode {

    private final String varName;
    private final String indexName;
    private final JSASTNode array;
    private final JSASTNode body;

    public JSForEachNode(@NotNull String varName, @Nullable String indexName, JSASTNode array, JSASTNode body) {
        this.varName = varName;
        this.indexName = indexName;
        this.array = array;
        this.body = body;
    }

    @Override
    public JSObject evaluate(JSContext context) throws JSEvalException {
        JSArray arrayValue = array.evaluate(context).asArray();
        for (int i = 0; i < arrayValue.getValues().size(); i++) {
            JSContext pushed = context.push();
            pushed.let(varName, arrayValue.getValues().get(i));
            if (indexName != null) pushed.let(indexName, new JSNumberObject(i));
            body.evaluate(pushed);
        }
        return JSUndefinedObject.INSTANCE;
    }

    @Override
    public void print(IndentedLogger logger) {
        logger.println("FOREACH " + varName);
        logger.println("ARRAY)");
        logger.pushprint(array);
        logger.println("BODY)");
        logger.pushprint(body);
    }
}
