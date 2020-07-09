package p0nki.pesl.api.parse;

import p0nki.pesl.api.object.ObjectType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface PESLVerifiable {

    void validate(Set<TreeRequirement> requirements) throws PESLValidateException;

    default void validate(TreeRequirement... newRequirements) throws PESLValidateException {
        validate(new HashSet<>(Arrays.asList(newRequirements)));
    }

    default void check(Set<TreeRequirement> requirements, ObjectType type) throws PESLValidateException {
        for (TreeRequirement requirement : requirements) {
            if (!requirement.validateType(type)) {
                throw validateError("Requirement " + requirement.toString() + " failed");
            }
        }
    }

    default void validate(Set<TreeRequirement> requirements, TreeRequirement... newRequirements) throws PESLValidateException {
        Set<TreeRequirement> set = new HashSet<>();
        set.addAll(requirements);
        set.addAll(Arrays.asList(newRequirements));
        validate(set);
    }

    default PESLValidateException validateError(String message) {
        return new PESLValidateException(this, message);
    }

    void print(@Nonnull PESLIndentedLogger logger);

}
