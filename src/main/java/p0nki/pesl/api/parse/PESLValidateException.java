package p0nki.pesl.api.parse;

public class PESLValidateException extends Exception {

    private final PESLVerifiable node;

    public PESLValidateException(PESLVerifiable node, String message) {
        super(message);
        this.node = node;
    }

    public PESLVerifiable getNode() {
        return node;
    }
}
