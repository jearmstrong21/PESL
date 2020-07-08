package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayLikeObject;
import p0nki.pesl.api.object.ArrayObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
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

    private void iterate(List<ArrayLikeObject> lists, List<Integer> currentIndices, List<PESLObject> result, PESLContext pushed) throws PESLEvalException {
        if (currentIndices.size() == lists.size()) {
            // do a thing
            for (int i = 0; i < fors.size(); i++) {
                pushed.let(fors.get(i).value, lists.get(i).getElement(currentIndices.get(i)));
                if (fors.get(i).index != null) pushed.let(fors.get(i).index, new NumberObject(currentIndices.get(i)));
            }
//            System.out.println("EVAL " + currentIndices);
            if (predicate == null || predicate.evaluate(pushed).asBoolean().getValue())
                result.add(element.evaluate(pushed));
        } else {
            for (int i = 0; i < lists.get(currentIndices.size()).arraySize(); i++) {
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
        List<ArrayLikeObject> lists = new ArrayList<>();
        for (For f : fors) {
            lists.add(f.list.evaluate(context).asArrayLike());
        }
        iterate(lists, new ArrayList<>(), result, context.push());
        return new ArrayObject(result);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("ARRAY COMPREHENSION)");
        for (int i = 0; i < fors.size(); i++) {
            logger.println("FOR " + i + ")");
            logger.println(fors.get(i).value + ", " + fors.get(i).index);
            logger.pushPrint(fors.get(i).list);
        }
        logger.println("PREDICATE)");
        logger.pushPrint(predicate);
    }

    public static class For {

        private final String value;
        private final String index;
        private final ASTNode list;

        public For(String value, @Nullable String index, ASTNode list) {
            this.value = value;
            this.index = index;
            this.list = list;
        }

    }
}
