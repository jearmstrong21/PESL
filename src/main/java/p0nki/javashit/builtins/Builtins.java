package p0nki.javashit.builtins;

import p0nki.javashit.object.*;
import p0nki.javashit.run.JSEvalException;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Builtins {

    public static final JSFunction PRINTLN = JSFunction.of(arguments -> {
        System.out.print("stdout >> ");
        for (JSObject object : arguments) {
            System.out.print(object.castToString() + " ");
        }
        System.out.println();
        return JSUndefinedObject.INSTANCE;
    });

    public static final JSFunction DIR = JSFunction.of(arguments -> {
        JSEvalException.validArgumentListLength(arguments, 1);
        return new JSArray(arguments.get(0).asMapLike().keys().stream().map(JSStringObject::new).collect(Collectors.toList()));
    });

    public static final JSMap MATH = new JSMap(new HashMap<>())
            .builderSet("random", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 0);
                return new JSNumberObject(Math.random());
            }))
            .builderSet("sqrt", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.sqrt(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("floor", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.floor(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("ceil", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.ceil(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("pow", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 2);
                return new JSNumberObject(Math.pow(arguments.get(0).asNumber().getValue(), arguments.get(1).asNumber().getValue()));
            }))
            .builderSet("abs", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.abs(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("sin", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.sin(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("cos", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.cos(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("tan", JSFunction.of(arguments -> {
                JSEvalException.validArgumentListLength(arguments, 1);
                return new JSNumberObject(Math.tan(arguments.get(0).asNumber().getValue()));
            }))
            .builderSet("min", JSFunction.of(arguments -> {
                if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                double min = arguments.get(0).asNumber().getValue();
                for (int i = 1; i < arguments.size(); i++) {
                    min = Math.min(min, arguments.get(i).asNumber().getValue());
                }
                return new JSNumberObject(min);
            }))
            .builderSet("max", JSFunction.of(arguments -> {
                if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                double max = arguments.get(0).asNumber().getValue();
                for (int i = 1; i < arguments.size(); i++) {
                    max = Math.max(max, arguments.get(i).asNumber().getValue());
                }
                return new JSNumberObject(max);
            }))
            .builderSet("any", JSFunction.of(arguments -> {
                if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                boolean value = arguments.get(0).asBoolean().getValue();
                for (int i = 1; i < arguments.size() && !value; i++) {
                    value = arguments.get(i).asBoolean().getValue();
                }
                return new JSBooleanObject(value);
            }))
            .builderSet("all", JSFunction.of(arguments -> {
                if (arguments.size() == 0) throw JSEvalException.INVALID_ARGUMENT_LIST;
                if (arguments.get(0) instanceof JSArray) arguments = ((JSArray) arguments.get(0)).getValues();
                boolean value = arguments.get(0).asBoolean().getValue();
                for (int i = 1; i < arguments.size() && value; i++) {
                    value = arguments.get(i).asBoolean().getValue();
                }
                return new JSBooleanObject(value);
            }));

}
