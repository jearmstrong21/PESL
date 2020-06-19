package p0nki.javashit.token;

import p0nki.javashit.token.JSTokenizeException;

public class JSCodeReader {

    private final String buffer;
    private int index;

    public JSCodeReader(String buffer) {
        this.buffer = buffer;
        index = 0;
    }

    public char next() throws JSTokenizeException {
        if (index >= buffer.length()) throw JSTokenizeException.UNEXPECTED_EOF;
        return buffer.charAt(index++);
    }

    public boolean canRead() {
        return index < buffer.length();
    }

}
