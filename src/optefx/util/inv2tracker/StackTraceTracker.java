
package optefx.util.inv2tracker;

import java.util.WeakHashMap;

/**
 *
 * @author Enrique Urra C.
 */
public class StackTraceTracker extends InvocationTracker
{    
    private class InvocationStackTrace implements InvocationTrace
    {
        private Object traced;
        private StackTraceElement stackTrace;

        public InvocationStackTrace(Object traceable, StackTraceElement stackTrace)
        {
            this.traced = traceable;
            this.stackTrace = stackTrace;
        }

        @Override
        public String getTraceDetails()
        {
            return traced + " - at " + stackTrace.toString();
        }
    }
    
    private WeakHashMap<Object, StackTraceElement> stackMap;

    public StackTraceTracker()
    {
        this.stackMap = new WeakHashMap<>();
    }

    private StackTraceElement getCurrentStackCall()
    {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[3];
    }

    @Override
    public <T> T trace(T elem)
    {        
        if(elem == null)
            throw new NullPointerException("Null elem");
        
        StackTraceElement stackCall = getCurrentStackCall();        
        stackMap.put(elem, stackCall);
        
        return elem;
    }

    @Override
    public boolean isTraced(Object elem)
    {
        return stackMap.containsKey(elem);
    }

    @Override
    public InvocationTrace getTraceFor(Object elem)
    {
        if(!stackMap.containsKey(elem))
            return null;
        
        return new InvocationStackTrace(elem, stackMap.get(elem));
    }
}
