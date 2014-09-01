
package optefx.util.metadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 *
 * @author Enrique Urra C.
 */
class WeakMapManager extends MetadataManager
{
    private final WeakHashMap<Object, List<Metadata>> cachedData;

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
    public <T extends Metadata> T[] getAllDataFor(Object target, Class<T> dataType)
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
        
        int resCount = specificData.size();
        T[] result = (T[])Array.newInstance(dataType, resCount);
        
        for(int i = 0; i < resCount; i++)
            result[i] = (T)specificData.get(i);
        
        return result;
    }
    
    @Override
    public boolean hasData(Object target, Class<? extends Metadata> dataType)
    {
        return getAllDataFor(target, dataType) != null;
    }
}
