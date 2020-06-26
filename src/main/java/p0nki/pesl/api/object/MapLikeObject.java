package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.Nonnull;
import java.util.Set;

public interface MapLikeObject {

    @Nonnull
    PESLObject get(String key) throws PESLEvalException;

    void set(@Nonnull String key, @Nonnull PESLObject value) throws PESLEvalException;

    @Nonnull
    Set<String> keys();

}
