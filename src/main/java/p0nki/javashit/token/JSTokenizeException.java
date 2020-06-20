package p0nki.javashit.token;

public class JSTokenizeException extends Exception {

    private final int index;

    public JSTokenizeException(String message, int index) {
        super(message);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    //    public JSTokenException(JSToken got, JSTokenType... expected) {
//        super("Expected " + Arrays.stream(expected).map(JSTokenType::toString).collect(Collectors.joining(", ")) + ", got " + got.toString());
//    }

}
