package p0nki.pesl.api.object;

import p0nki.pesl.api.PESLEvalException;

import javax.annotation.Nonnull;

public interface ArrayLikeObject {

    @Nonnull
    PESLObject getElement(int index) throws PESLEvalException;

    @Nonnull
    default PESLObject getElementNoChecks(int index) {
        try {
            return getElement(index);
        } catch (PESLEvalException e) {
            throw new AssertionError(e);
        }
    }

    void setElement(int index, @Nonnull PESLObject object) throws PESLEvalException;

    @Nonnull
    PESLObject removeElement(int index) throws PESLEvalException;

    int arraySize();

}
