package p0nki.javashit;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class Utilities {

    private Utilities() {

    }

    public static OptionalInt parseInt(String str) {
        try {
            return OptionalInt.of(Integer.parseInt(str));
        } catch (NumberFormatException ignored) {
            return OptionalInt.empty();
        }
    }

    public static OptionalDouble parseDouble(String str) {
        try {
            return OptionalDouble.of(Double.parseDouble(str));
        } catch (NumberFormatException ignored) {
            return OptionalDouble.empty();
        }
    }

}
