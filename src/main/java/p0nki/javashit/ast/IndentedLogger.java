package p0nki.javashit.ast;

import p0nki.javashit.ast.nodes.JSASTNode;

public class IndentedLogger {

    private int indent;

    public IndentedLogger() {
        indent = 0;
    }

    public void push() {
        indent++;
    }

    public void pop() {
        indent--;
    }

    public void println(String str) {
        for (int i = 0; i < indent; i++) System.out.print("    ");
        System.out.println(str);
    }

    public void pushprint(JSASTNode node) {
        push();
        if (node == null) println("[NULL]");
        else node.print(this);
        pop();
    }

}
