package p0nki.pesl.api.parse;

import p0nki.pesl.api.object.BooleanObject;
import p0nki.pesl.api.object.FunctionObject;
import p0nki.pesl.api.object.NumberObject;
import p0nki.pesl.api.object.ObjectType;

public interface TreeRequirement {

    TreeRequirement NUMBER = withName("NUMBER", type -> type.getDeclaredType().equals(NumberObject.TYPE));
    TreeRequirement ARRAYLIKE = withName("ARRAYLIKE", ObjectType::isArrayLike);
    TreeRequirement MAPLIKE = withName("MAPLIKE", ObjectType::isMapLike);
    TreeRequirement BOOLEAN = withName("BOOLEAN", type -> type.getDeclaredType().equals(BooleanObject.TYPE));
    TreeRequirement FUNCTION = withName("FUNCTION", type -> type.getDeclaredType().equals(FunctionObject.TYPE));

    static TreeRequirement withName(String name, TreeRequirement requirement) {
        return new TreeRequirement() {
            @Override
            public boolean validateType(ObjectType type) {
                return requirement.validateType(type);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    static TreeRequirement or(TreeRequirement a, TreeRequirement b) {
        return withName(String.format("OR[%s,%s]", a, b), type -> a.validateType(type) || b.validateType(type));
    }

    boolean validateType(ObjectType type);

}
