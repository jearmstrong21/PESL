package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayLikeObject;
import p0nki.pesl.api.object.MapLikeObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.parse.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class ComprehensionFor {

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