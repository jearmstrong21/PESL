package p0nki.pesl.api.builtins;

import p0nki.pesl.api.PESLEvalException;
import p0nki.pesl.api.object.*;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PESLBuiltins {

    public static final FunctionObject PRINTLN = FunctionObject.of(false, arguments -> {
        System.out.print("stdout >> ");
        for (PESLObject object : arguments) {
            System.out.print(object.castToString() + " ");
        }
        System.out.println();
        return UndefinedObject.INSTANCE;
    });

    public static final FunctionObject TYPEOF = FunctionObject.of(false, arguments -> {
        PESLEvalException.validArgumentListLength(arguments, 1);
        return new StringObject(arguments.get(0).getType());
    });

    public static final FunctionObject DIR = FunctionObject.of(false, arguments -> {
        PESLEvalException.validArgumentListLength(arguments, 1);
        return new ArrayObject(arguments.get(0).asMapLike().keys().stream().map(StringObject::new).collect(Collectors.toList()));
    });

    public static final MapObject MATH = new MapObject(new HashMap<>())
            .builderSet("random", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 0);
                return new NumberObject(Math.random());
            }))
            .builderSet("sqrt", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.sqrt(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("floor", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.floor(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("ceil", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.ceil(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("pow", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 2);
                return new NumberObject(Math.pow(arguments.get(0).asNumber().getValue(), arguments.get(1).asNumber().getValue()));
            }))
            .builderSet("abs", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.abs(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("sin", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.sin(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("cos", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.cos(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("tan", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new NumberObject(Math.tan(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("min", FunctionObject.of(true, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                double min = arguments.get(0).asNumber().getValue();
                for (int i = 1; i < arguments.size(); i++) {
                    min = Math.min(min, arguments.get(i).asNumber().getValue());
                }
                return new NumberObject(min);
            }))
            .builderSet("max", FunctionObject.of(true, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                double max = arguments.get(0).asNumber().getValue();
                for (int i = 1; i < arguments.size(); i++) {
                    max = Math.max(max, arguments.get(i).asNumber().getValue());
                }
                return new NumberObject(max);
            }))
            .builderSet("any", FunctionObject.of(true, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                boolean value = arguments.get(0).asBoolean().getValue();
                for (int i = 1; i < arguments.size() && !value; i++) {
                    value = arguments.get(i).asBoolean().getValue();
                }
                return new BooleanObject(value);
            }))
            .builderSet("all", FunctionObject.of(true, arguments -> {
                if (arguments.size() == 0) throw PESLEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof ArrayObject) arguments = ((ArrayObject) arguments.get(0)).getValues();
                boolean value = arguments.get(0).asBoolean().getValue();
                for (int i = 1; i < arguments.size() && value; i++) {
                    value = arguments.get(i).asBoolean().getValue();
                }
                return new BooleanObject(value);
            }));

    public static final MapObject DATA = new MapObject(new HashMap<>())
            .builderSet("write", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    PESLDataUtils.write(arguments.get(0), new DataOutputStream(outputStream));
                } catch (IOException e) {
                    throw new PESLEvalException(e.getMessage());
                }
                return new StringObject(outputStream.toString());
            }))
            .builderSet("read", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(arguments.get(0).asString().getValue().getBytes());
                try {
                    return PESLDataUtils.read(new DataInputStream(inputStream));
                } catch (IOException e) {
                    throw new PESLEvalException(e.getMessage());
                }
            }))
            .builderSet("copy", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return PESLDataUtils.copy(arguments.get(0));
            }));

    public static final MapObject SYSTEM = new MapObject(new HashMap<>())
            .builderSet("time", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 0);
                return new NumberObject(System.currentTimeMillis());
            }))
            .builderSet("formatDate", FunctionObject.of(true, arguments -> {
                PESLEvalException.validArgumentListLength(arguments, 1);
                return new StringObject(new Date((long) arguments.get(0).asNumber().getValue()).toString());
            }));

}
