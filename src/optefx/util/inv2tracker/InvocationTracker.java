
package optefx.util.inv2tracker;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class InvocationTracker
{
    private static InvocationTracker instance;
    
    public static InvocationTracker getInstance()
    {
        if(instance == null)
            instance = new StackTraceTracker();
        
        return instance;
    }
    
    public abstract <T> T trace(T elem);
    public abstract boolean isTraced(Object elem);
    public abstract InvocationTrace getTraceFor(Object elem);
}
