package p0nki.pesl.internal.nodes;

import p0nki.pesl.api.PESLContext;
import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.ArrayLikeObject;
import p0nki.pesl.api.object.MapLikeObject;
import p0nki.pesl.api.object.PESLObject;
import p0nki.pesl.api.object.UndefinedObject;
import p0nki.pesl.api.parse.ASTNode;
import p0nki.pesl.api.parse.PESLIndentedLogger;
import p0nki.pesl.api.parse.PESLValidateException;
import p0nki.pesl.api.parse.TreeRequirement;

import javax.annotation.Nonnull;
import java.util.Set;

public class DeleteNode implements ASTNode {

    private final ASTNode deleteNode;

    public DeleteNode(ASTNode deleteNode) {
        this.deleteNode = deleteNode;
    }

    @Nonnull
    @Override
    public PESLObject evaluate(@Nonnull PESLContext context) throws PESLEvalException {
        if (deleteNode instanceof AccessPropertyNode) {
            PESLObject value = ((AccessPropertyNode) deleteNode).getValue().evaluate(context);
            PESLObject key = ((AccessPropertyNode) deleteNode).getKey().evaluate(context);
            if (value instanceof ArrayLikeObject) {
                return ((ArrayLikeObject) value).removeElement((int) key.asNumber().getValue());
            } else {
                MapLikeObject mapLike = value.asMapLike();
                String keyValue = key.castToString();
                PESLObject result = mapLike.getKey(keyValue);
                mapLike.setKey(keyValue, UndefinedObject.INSTANCE);
                return result;
            }
        } else {
            throw new PESLEvalException("Cannot delete this value");
        }
    }

    @Override
    public void validate(Set<TreeRequirement> requirements) throws PESLValidateException {
        if (!(deleteNode instanceof AccessPropertyNode)) throw validateError("Cannot delete non-access");
        ((AccessPropertyNode) deleteNode).getKey().validate(TreeRequirement.or(TreeRequirement.ARRAYLIKE, TreeRequirement.MAPLIKE));
    }

    @Override
    public void print(@Nonnull PESLIndentedLogger logger) {
        logger.println("DELETE");
        logger.pushPrint(deleteNode);
    }
}
