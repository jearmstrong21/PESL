package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayObject;
import p0nki.pesl.api.object.FunctionObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FunctionInvokeNode implements ASTNode {

    private final ASTNode function;
    private final List<ASTNode> arguments;

    public FunctionInvokeNode(ASTNode function, List<ASTNode> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public @javax.annotation.Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        FunctionObject functionValue = function.evaluate(context).asFunction();
        PESLContext newContext = context.push();
        List<PESLObject> evaluatedArguments = new ArrayList<>();
        if (functionValue.getThisObject() != null) {
            evaluatedArguments.add(functionValue.getThisObject());
        }
        for (ASTNode argument : arguments) {
            evaluatedArguments.add(argument.evaluate(context));
        }
        for (int i = 0; i < functionValue.getArgumentNames().size(); i++) {
            if (i < evaluatedArguments.size()) {
                newContext.let(functionValue.getArgumentNames().get(i), evaluatedArguments.get(i));
            } else {
                newContext.let(functionValue.getArgumentNames().get(i), UndefinedObject.INSTANCE);
            }
        }
        newContext.setKey("arguments", new ArrayObject(evaluatedArguments));
        return functionValue.getNode().evaluate(newContext);
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        for (ASTNode node : arguments) {
            node.validate();
        }
        function.validate(TreeRequirement.FUNCTION);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("FUNCTION INVOKE");
        logger.println("FUNC)");
        logger.pushPrint(function);
        logger.println("ARGUMENTS)");
        logger.push();
        for (int i = 0; i < arguments.size(); i++) {
            logger.println(i + ")");
            logger.pushPrint(arguments.get(i));
        }
        logger.pop();
    }

}
