package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.Nonnull;
import java.util.Set;

public interface MapLikeObject {

    @Nonnull
    PESLObject getKey(@Nonnull String key);

    PESLObject setKey(@Nonnull String key, @Nonnull PESLObject value) throws PESLEvalException;

    boolean containsKey(@Nonnull String key);

    @Nonnull
    Set<String> keys();

}
