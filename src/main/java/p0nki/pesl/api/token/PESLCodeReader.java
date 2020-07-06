package p0nki.pesl.api.token;

import javax.annotation.Nonnull;

public class PESLCodeReader {

    private final String buffer;
    private int index;

    public PESLCodeReader(@Nonnull String buffer) {
        this.buffer = buffer;
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public char next() throws PESLTokenizeException {
        if (index >= buffer.length()) throw new PESLTokenizeException("Unexpected EOF", buffer.length());
        return buffer.charAt(index++);
    }

    public char peek() throws PESLTokenizeException {
        if (index >= buffer.length()) throw new PESLTokenizeException("Unexpected EOF", buffer.length());
        return buffer.charAt(index);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean canRead() {
        return index < buffer.length();
    }

}
