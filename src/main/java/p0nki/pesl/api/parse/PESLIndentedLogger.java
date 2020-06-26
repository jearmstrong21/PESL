package p0nki.pesl.api.parse;

import javax.annotation.Nullable;

public class PESLIndentedLogger {

    private int indent;

    public PESLIndentedLogger() {
        indent = 0;
    }

    public void push() {
        indent++;
    }

    public void pop() {
        indent--;
    }

    public void println(@Nullable String str) {
        for (int i = 0; i < indent; i++) System.out.print("    ");
        System.out.println(str);
    }

    public void pushPrint(@Nullable ASTNode node) {
        push();
        if (node == null) println("[NULL]");
        else node.print(this);
        pop();
    }

}
