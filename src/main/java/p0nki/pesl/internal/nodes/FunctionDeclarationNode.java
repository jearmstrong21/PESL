package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.object.FunctionObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;
import java.util.List;

public class FunctionDeclarationNode implements ASTNode {

    private final List<String> argumentNames;
    private final ASTNode body;

    public FunctionDeclarationNode(List<String> argumentNames, ASTNode body) {
        this.argumentNames = argumentNames;
        this.body = body;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) {
        return new FunctionObject(null, argumentNames, body);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("FUNC DECLARE");
        logger.println("ARGUMENT NAMES (" + String.join(", ", argumentNames) + ")");
        logger.println("BODY)");
        logger.pushPrint(body);
    }
}
