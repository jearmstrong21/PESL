package p0nki.javashit.token;

public class JSCodeReader {

    private final String buffer;
    private int index;

    public JSCodeReader(String buffer) {
        this.buffer = buffer;
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public char next() throws JSTokenizeException {
        if (index >= buffer.length()) throw new JSTokenizeException("Unexpected EOF", buffer.length());
        return buffer.charAt(index++);
    }

    public boolean canRead() {
        return index < buffer.length();
    }

}
