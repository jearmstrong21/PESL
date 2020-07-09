package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayLikeObject;
import p0nki.pesl.api.object.MapLikeObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ComprehensionFor implements PESLVerifiable {

    private final String first;
    private final String second;
    private final boolean isArray;
    private final ASTNode value;

    public ComprehensionFor(String first, String second, boolean isArray, ASTNode value) {
        this.first = first;
        this.second = second;
        this.isArray = isArray;
        this.value = value;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public boolean isArray() {
        return isArray;
    }

    public ASTNode getValue() {
        return value;
    }

    public List<?> asList(PESLContext context) throws PESLEvalException {
        PESLObject object = value.evaluate(context);
        if (isArray) {
            ArrayLikeObject arrayLikeObject = object.asArrayLike();
            List<PESLObject> list = new ArrayList<>();
            for (int i = 0; i < arrayLikeObject.arraySize(); i++) {
                list.add(arrayLikeObject.getElement(i));
            }
            return list;
        } else {
            MapLikeObject mapLikeObject = object.asMapLike();
            List<Entry> list = new ArrayList<>();
            for (String key : mapLikeObject.keys()) {
                list.add(new Entry(key, mapLikeObject.getKey(key)));
            }
            return list;
        }
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        if (isArray) value.validate(TreeRequirement.ARRAYLIKE);
        else value.validate(TreeRequirement.MAPLIKE);
        if (requirements.size() > 0)
            throw validateError("Cannot require anything of ComprehensionFor since it does not produce value");
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println(String.format("FOR) %s, %s, %s", first, second, isArray ? "array" : "map"));
        logger.pushPrint(value);
    }


    static class Entry {

        private final String first;
        private final PESLObject second;

        public Entry(String first, PESLObject second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public PESLObject getSecond() {
            return second;
        }
    }

}