package net.afterlifelochie.fontbox.api.tracer;

/**
 * <p>
 * Void tracer. Does exactly that - void. You should use this in place of
 * passing null to the tracer arguments in method calls.
 * </p>
 *
 * @author AfterLifeLochie
 */
public class VoidTracer implements ITracer {
    @Override
    public void trace(Object... params) {
        /* Do nothing */
    }

    @Override
    public void warn(Object... params) {
        StringBuilder result = new StringBuilder();
        for (Object element : params)
            result.append(element).append(", ");
        String r0 = result.toString();
        System.out.println("FontRegistry warning: " + r0.substring(0, r0.length() - 2));
    }

    @Override
    public boolean enableAssertion() {
        return false;
    }

    @Override
    public boolean ignoreInvalidSymbols() {
        return true;
    }
}
