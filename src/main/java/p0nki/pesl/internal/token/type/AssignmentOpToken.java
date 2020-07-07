package p0nki.pesl.internal.token.type;

public class AssignmentOpToken extends PESLToken {

    private final AssignmentType opType;

    public AssignmentOpToken(AssignmentType opType, int start, int end) {
        super(TokenType.ASSIGNMENT_OP, start, end);
        this.opType = opType;
    }

    public AssignmentType getOpType() {
        return opType;
    }

    @Override
    public String toString() {
        return "AssignmentOpToken[" + opType + "]";
    }
}
