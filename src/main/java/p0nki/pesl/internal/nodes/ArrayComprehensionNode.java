package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.*;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ArrayComprehensionNode implements ASTNode {

    private final ASTNode element;
    private final List<For> fors;
    private final ASTNode predicate;

    public ArrayComprehensionNode(ASTNode element, List<For> fors, @Nullable ASTNode predicate) {
        this.element = element;
        this.fors = fors;
        this.predicate = predicate;
    }

    private void iterate(List<List<?>> lists, List<Integer> currentIndices, List<PESLObject> result, PESLContext pushed) throws PESLEvalException {
        if (currentIndices.size() == lists.size()) {
            for (int i = 0; i < fors.size(); i++) {
                Object object = lists.get(i).get(currentIndices.get(i));
                if (fors.get(i).isArray) {
                    pushed.let(fors.get(i).first, (PESLObject) object);
                    if (fors.get(i).second != null)
                        pushed.let(fors.get(i).second, new NumberObject(currentIndices.get(i)));
                } else {
                    pushed.let(fors.get(i).first, new StringObject(((Entry) object).first));
                    pushed.let(fors.get(i).second, ((Entry) object).second);
                }
            }
            if (predicate == null || predicate.evaluate(pushed).asBoolean().getValue())
                result.add(element.evaluate(pushed));
        } else {
            for (int i = 0; i < lists.get(currentIndices.size()).size(); i++) {
                List<Integer> newIndices = new ArrayList<>(currentIndices);
                newIndices.add(i);
                iterate(lists, newIndices, result, pushed);
            }
        }
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        List<PESLObject> result = new ArrayList<>();
        List<List<?>> lists = new ArrayList<>();
        for (For f : fors) {
            lists.add(f.asList(context));
        }
        iterate(lists, new ArrayList<>(), result, context.push());
        return new ArrayObject(result);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("ARRAY COMPREHENSION)");
        for (int i = 0; i < fors.size(); i++) {
            logger.println("FOR " + i + ") " + (fors.get(i).isArray ? "ARRAY" : "MAP"));
            logger.println(fors.get(i).first + ", " + fors.get(i).second);
            logger.pushPrint(fors.get(i).value);
        }
        logger.println("PREDICATE)");
        logger.pushPrint(predicate);
    }

    public static class For {

        private final String first;
        private final String second;
        private final boolean isArray;
        private final ASTNode value;

        public For(String first, String second, boolean isArray, ASTNode value) {
            this.first = first;
            this.second = second;
            this.isArray = isArray;
            this.value = value;
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

    }

    private static class Entry {

        private final String first;
        private final PESLObject second;

        public Entry(String first, PESLObject second) {
            this.first = first;
            this.second = second;
        }
    }
}
