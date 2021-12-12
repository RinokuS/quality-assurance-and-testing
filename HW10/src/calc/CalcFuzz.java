package calc;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CalcFuzz {

    public static void fuzzerTestOneInput(FuzzedDataProvider provider) {
        try {
            double num = Calc.calculate(provider.consumeRemainingAsString());
        } catch (CalcException ignored) {}
    }

    static final String base64Bytes = "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAABdAACCgp4";

    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        try {
            Method fuzzerInitialize = calc.CalcFuzz.class.getMethod("fuzzerInitialize");
            fuzzerInitialize.invoke(null);
        } catch (NoSuchMethodException ignored) {
            try {
                Method fuzzerInitialize = calc.CalcFuzz.class.getMethod("fuzzerInitialize", String[].class);
                fuzzerInitialize.invoke(null, (Object) args);
            } catch (NoSuchMethodException ignored1) {
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(1);
        }
        com.code_intelligence.jazzer.api.CannedFuzzedDataProvider input = new com.code_intelligence.jazzer.api.CannedFuzzedDataProvider(base64Bytes);
        calc.CalcFuzz.fuzzerTestOneInput(input);
    }
}
