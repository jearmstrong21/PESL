package p0nki.pesl.api.parse;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.PESLObject;

import javax.annotation.Nonnull;

public interface ASTNode extends PESLVerifiable {

    @Nonnull
    PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException;

}
