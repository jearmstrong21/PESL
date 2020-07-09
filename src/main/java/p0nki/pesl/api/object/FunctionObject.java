package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FunctionObject extends PESLObject {

    private final PESLObject thisObject;
    private final ASTNode node;
    private final List<String> argumentNames;

    public static final String TYPE = "function";

    public FunctionObject(@Nullable PESLObject thisObject, @Nonnull List<String> argumentNames, @Nonnull ASTNode node) {
        super(TYPE);
        this.thisObject = thisObject;
        this.argumentNames = argumentNames;
        this.node = node;
    }

    @Nonnull
    public static FunctionObject of(boolean removeThisParameter, PESLFunctionInterface value) {
        return new FunctionObject(null, new ArrayList<>(), new ASTNode() {
            @Override
            public @Nonnull
            PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
                List<PESLObject> arguments = context.getKey("arguments").asArray().getValues();
                if (removeThisParameter) {
                    if (arguments.size() == 0)
                        throw new PESLEvalException("Expected to remove `this` parameter, but none were found. This means that you wrote your builtins wrong.");
                    arguments.remove(0);
                }
                return value.operate(arguments);
            }

            @Override
            public void validate(Set<TreeRequirement> requirements) {

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

    @Override
    public boolean compareEquals(@Nonnull PESLObject object) {
        return false;
    }

    @FunctionalInterface
    public interface PESLFunctionInterface {

        @Nonnull
        PESLObject operate(@Nonnull List<PESLObject> arguments) throws PESLEvalException;

    }
}
