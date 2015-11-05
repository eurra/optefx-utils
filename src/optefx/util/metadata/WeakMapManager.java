
package optefx.util.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 *
 * @author Enrique Urra C.
 */
class WeakMapManager extends MetadataManager
{
    private WeakHashMap<Object, List<Metadata>> cachedData;

    public WeakMapManager()
    {
        cachedData = new WeakHashMap<>();
    }

    @Override
    public void attachData(Object target, Metadata metadata)
    {
        List<Metadata> currData = cachedData.get(target);
        
        if(currData == null)
        {
            currData = new ArrayList<>();
            cachedData.put(target, currData);
        }
        
        currData.add(metadata);
    }

    @Override
    public <T extends Metadata> T getDataFor(Object target, Class<T> dataType)
    {
        List<Metadata> currData = cachedData.get(target);
        
        if(currData == null)
            return null;
        
        for(Metadata data : currData)
        {
            if(dataType.isAssignableFrom(data.getClass()))
                return (T)data;
        }
        
        return null;
    }

    @Override
    public <T extends Metadata> Metadata[] getAllDataFor(Object target, Class<T> dataType)
    {
        List<Metadata> currData = cachedData.get(target);
        
        if(currData == null)
            return null;
        
        List<Metadata> specificData = new ArrayList<>();
        
        for(Metadata data : currData)
        {
            if(dataType.isAssignableFrom(data.getClass()))
                specificData.add(data);
        }
        
        return specificData.toArray(new Metadata[0]);
    }
    
    @Override
    public <T extends Metadata> boolean hasData(Object target, Class<T> dataType)
    {
        return getAllDataFor(target, dataType) != null;
    }
}
