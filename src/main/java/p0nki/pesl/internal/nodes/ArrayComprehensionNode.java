package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.StringObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ArrayComprehensionNode implements ASTNode {

    private final ASTNode element;
    private final List<ComprehensionFor> fors;
    private final ASTNode predicate;

    public ArrayComprehensionNode(ASTNode element, List<ComprehensionFor> fors, @Nullable ASTNode predicate) {
        this.element = element;
        this.fors = fors;
        this.predicate = predicate;
    }

    private void iterate(List<List<?>> lists, List<Integer> currentIndices, List<PESLObject> result, PESLContext pushed) throws PESLEvalException {
        if (currentIndices.size() == lists.size()) {
            for (int i = 0; i < fors.size(); i++) {
                Object object = lists.get(i).get(currentIndices.get(i));
                if (fors.get(i).isArray()) {
                    pushed.let(fors.get(i).getFirst(), (PESLObject) object);
                    if (fors.get(i).getSecond() != null)
                        pushed.let(fors.get(i).getSecond(), new NumberObject(currentIndices.get(i)));
                } else {
                    pushed.let(fors.get(i).getFirst(), new StringObject(((ComprehensionFor.Entry) object).getFirst()));
                    pushed.let(fors.get(i).getSecond(), ((ComprehensionFor.Entry) object).getSecond());
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
        for (ComprehensionFor f : fors) {
            lists.add(f.asList(context));
        }
        iterate(lists, new ArrayList<>(), result, context.push());
        return new ArrayObject(result);
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("ARRAY COMPREHENSION)");
        for (int i = 0; i < fors.size(); i++) {
            logger.println("FOR " + i + ") " + (fors.get(i).isArray() ? "ARRAY" : "MAP"));
            logger.println(fors.get(i).getFirst() + ", " + fors.get(i).getSecond());
            logger.pushPrint(fors.get(i).getValue());
        }
        logger.println("PREDICATE)");
        logger.pushPrint(predicate);
    }
}
