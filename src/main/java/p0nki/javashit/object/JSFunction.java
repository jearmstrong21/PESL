package p0nki.javashit.object;

import p0nki.javashit.ast.nodes.JSASTNode;

import java.util.List;

public class JSFunction extends JSObject{

    private final List<String> argumentNames;
    private final JSASTNode node;

    public JSFunction(List<String> argumentNames, JSASTNode node) {
        this.argumentNames = argumentNames;
        this.node = node;
    }

    public JSASTNode getNode() {
        return node;
    }

    public List<String> getArgumentNames() {
        return argumentNames;
    }

    //TODO better stringify with argument names
    @Override
    public String stringify() {
        return "function { ... code ... }";
    }

    @Override
    public JSObjectType type() {
        return JSObjectType.FUNCTION;
    }

    @Override
    public String castToString() {
        return "function { ... code ... }";
    }
}
