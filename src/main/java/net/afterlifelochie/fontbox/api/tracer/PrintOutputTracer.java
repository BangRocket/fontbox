package net.afterlifelochie.fontbox.api.tracer;

/**
 * Print output tracer. Very verbose debugger; prints all trace calls to the
 * console. Avoid using this unless you really need to see all the information
 * from the trace.
 *
 * @author AfterLifeLochie
 */
public class PrintOutputTracer implements ITracer {
    @Override
    public void trace(Object... params) {
        StringBuilder result = new StringBuilder();
        for (Object element : params)
            result.append(element).append(", ");
        String r0 = result.toString();
        System.out.println("PrintOutputTracer.trace: " + r0.substring(0, r0.length() - 2));
    }

    @Override
    public void warn(Object... params) {
        StringBuilder result = new StringBuilder();
        for (Object element : params)
            result.append(element).append(", ");
        String r0 = result.toString();
        System.out.println("PrintOutputTracer.warn: " + r0.substring(0, r0.length() - 2));
    }

    @Override
    public boolean enableAssertion() {
        return true;
    }
}
