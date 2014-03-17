
package optefx.util.metadata;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class MetadataManager
{
    private static MetadataManager instance;
    
    public static MetadataManager getInstance()
    {
        if(instance == null)
            instance = new WeakMapManager();
        
        return instance;
    }
    
    public abstract void attachData(Object target, Metadata metadata);
    public abstract <T extends Metadata> T getDataFor(Object target, Class<T> dataType);
    public abstract <T extends Metadata> Metadata[] getAllDataFor(Object target, Class<T> dataType);
    public abstract <T extends Metadata> boolean hasData(Object target, Class<T> dataType);
}
