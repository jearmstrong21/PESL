package p0nki.pesl.api.builtins;

import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.*;
import p0nki.pesl.util.OpenSimplexNoise;

import java.io.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PESLBuiltins {

    public static final PESLObject RANGE = FunctionObject.of(false, arguments -> {
        PESLEvalException.validArgumentListLength(arguments, 1, 2);
        if (arguments.size() == 1) {
            return new ArrayObject(IntStream.range(0, (int) arguments.get(0).asNumber().getValue()).boxed().map(NumberObject::new).collect(Collectors.toList()));
        } else {
            return new ArrayObject(IntStream.range((int) arguments.get(0).asNumber().getValue(), (int) arguments.get(1).asNumber().getValue()).boxed().map(NumberObject::new).collect(Collectors.toList()));
        }
    });

    public static final PESLObject PRINTLN = FunctionObject.of(false, arguments -> {
        System.out.print("stdout >> ");
        for (PESLObject object : arguments) {
            System.out.print(object.castToString() + " ");
        }
        System.out.println();
        return UndefinedObject.INSTANCE;
    });

    public static final PESLObject TYPEOF = FunctionObject.of(false, arguments -> {
        PESLEvalException.validArgumentListLength(arguments, 1);
        return new StringObject(arguments.get(0).getType());
    });

    public static final PESLObject DIR = FunctionObject.of(false, arguments -> {
        PESLEvalException.validArgumentListLength(arguments, 1);
        return new ArrayObject(arguments.get(0).asMapLike().keys().stream().map(StringObject::new).collect(Collectors.toList()));
    });

    public static final PESLObject MATH = BuiltinMapLikeObject.builtinBuilder()
            .put("random", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 0);
                return new NumberObject(Math.random());
            }))
            .put("sqrt", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.sqrt(arguments.get(0).asNumber().getValue()));
            }))
            .put("floor", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.floor(arguments.get(0).asNumber().getValue()));
            }))
            .put("ceil", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.ceil(arguments.get(0).asNumber().getValue()));
            }))
            .put("pow", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 2);
                return new NumberObject(Math.pow(arguments.get(0).asNumber().getValue(), arguments.get(1).asNumber().getValue()));
            }))
            .put("abs", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.abs(arguments.get(0).asNumber().getValue()));
            }))
            .put("sin", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.sin(arguments.get(0).asNumber().getValue()));
            }))
            .put("cos", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.cos(arguments.get(0).asNumber().getValue()));
            }))
            .put("tan", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.tan(arguments.get(0).asNumber().getValue()));
            }))
            .put("min", FunctionObject.of(false, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                double min = arguments.get(0).asNumber().getValue();
                for (int i = 1; i < arguments.size(); i++) {
                    min = Math.min(min, arguments.get(i).asNumber().getValue());
                }
                return new NumberObject(min);
            }))
            .put("max", FunctionObject.of(false, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                double max = arguments.get(0).asNumber().getValue();
                for (int i = 1; i < arguments.size(); i++) {
                    max = Math.max(max, arguments.get(i).asNumber().getValue());
                }
                return new NumberObject(max);
            }))
            .put("any", FunctionObject.of(false, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                boolean value = arguments.get(0).asBoolean().getValue();
                for (int i = 1; i < arguments.size() && !value; i++) {
                    value = arguments.get(i).asBoolean().getValue();
                }
                return new BooleanObject(value);
            }))
            .put("all", FunctionObject.of(false, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                boolean value = arguments.get(0).asBoolean().getValue();
                for (int i = 1; i < arguments.size() && value; i++) {
                    value = arguments.get(i).asBoolean().getValue();
                }
                return new BooleanObject(value);
            }))
            .put("noise", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1, 2);
                double y = 0;
                if (arguments.size() == 2) y = arguments.get(0).asNumber().getValue();
                return new NumberObject(OpenSimplexNoise.PESL_INSTANCE.sample(arguments.get(0).asNumber().getValue()));
            }));

    public static final PESLObject DATA = BuiltinMapLikeObject.builtinBuilder()
            .put("write", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    PESLDataUtils.write(arguments.get(0), new DataOutputStream(outputStream));
                } catch (IOException e) {
                    throw new PESLEvalException(e.getMessage());
                }
                return new StringObject(outputStream.toString());
            }))
            .put("read", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(arguments.get(0).asString().getValue().getBytes());
                try {
                    return PESLDataUtils.read(new DataInputStream(inputStream));
                } catch (IOException e) {
                    throw new PESLEvalException(e.getMessage());
                }
            }))
            .put("copy", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return PESLDataUtils.copy(arguments.get(0));
            }))
            .put("deepEquals", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 2);
                return new BooleanObject(PESLDataUtils.deepEquals(arguments.get(0), arguments.get(1)));
            }));

    public static final PESLObject PARSE_NUMBER = FunctionObject.of(false, arguments -> {
        PESLEvalException.validArgumentListLength(arguments, 1);
        try {
            return new NumberObject(Double.parseDouble(arguments.get(0).castToString()));
        } catch (NumberFormatException e) {
            throw new PESLEvalException("Cannot parse number");
        }
    });

    public static final PESLObject SYSTEM = BuiltinMapLikeObject.builtinBuilder()
            .put("time", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 0);
                return new NumberObject(System.currentTimeMillis());
            }))
            .put("formatDate", FunctionObject.of(false, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new StringObject(new Date((long) arguments.get(0).asNumber().getValue()).toString());
            }));

}
