package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FunctionObject extends PESLObject {

    private final PESLObject thisObject;
    private final ASTNode node;
    private final List<String> argumentNames;

    public FunctionObject(@Nullable PESLObject thisObject, @Nonnull List<String> argumentNames, @Nonnull ASTNode node) {
        super("function");
        this.thisObject = thisObject;
        this.argumentNames = argumentNames;
        this.node = node;
    }

    @Nonnull
    public static FunctionObject of(boolean removeThisParameter, JSFInterface jsfInterface) {
        return new FunctionObject(null, new ArrayList<>(), new ASTNode() {
            @Override
            public @Nonnull
            PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
                List<PESLObject> arguments = context.get("arguments").asArray().getValues();
                if (removeThisParameter) arguments.remove(0);
                return jsfInterface.operate(arguments);
            }

            @Override
            public void print(@Nonnull PESLIndentedLogger logger) {
                logger.println("[native function]");
            }
        });
    }

    @Nullable
    public PESLObject getThisObject() {
        return thisObject;
    }

    @Nonnull
    public ASTNode getNode() {
        return node;
    }

    @Nonnull
    public List<String> getArgumentNames() {
        return argumentNames;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String stringify() {
        return String.format("function(%s)", String.join(", ", argumentNames));
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String castToString() {
        return stringify();
    }

    @FunctionalInterface
    public interface JSFInterface {

        @Nonnull
        PESLObject operate(@Nonnull List<PESLObject> arguments) throws PESLEvalException;

    }
}
