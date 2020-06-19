package p0nki.javashit.ast.nodes;

import p0nki.javashit.ast.IndentedLogger;
import p0nki.javashit.object.JSObject;
import p0nki.javashit.run.JSContext;
import p0nki.javashit.run.JSEvalException;

public interface JSASTNode {

    JSObject evaluate(JSContext context) throws JSEvalException;

    void print(IndentedLogger logger);

}
