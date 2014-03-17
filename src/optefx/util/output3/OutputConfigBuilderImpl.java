
package optefx.util.output3;

import java.util.HashMap;
import java.util.Map;

/**
 * The default configuration builder implementation.
 * @author Enrique Urra C.
 */
class OutputConfigBuilderImpl implements OutputConfigBuilder
{
    /**
     * A map of identifiers, which are related to a particular output 
     * configuration.
     */
    private Map<String, OutputData> cfgs;

    /**
     * Default constructor.
     */
    public OutputConfigBuilderImpl()
    {
        this.cfgs = new HashMap<>();
    }
    
    /**
     * Gets an output configuration (initializing it if is necessary) based on
     * the provided id.
     * @param id The id of the output to get.
     * @return The output configuration object.
     */
    private OutputData getConfig(String id)
    {
        OutputData cfg = cfgs.get(id);
        
        if(cfg == null)
        {
            cfg = new OutputData();
            cfgs.put(id, cfg);
        }
        
        return cfg;
    }
    
    @Override
    public OutputConfigBuilder enableSystemOutput(String id)
    {
        OutputData cfg = getConfig(id);
        cfg.setSystemOut(true);
        
        return this;
    }

    @Override
    public OutputConfigBuilder addFileOutput(String id, String file, boolean append)
    {
        OutputData cfg = getConfig(id);
        cfg.addFileConfig(new FileConfig(file, append));
        
        return this;
    }

    @Override
    public OutputConfigBuilder setDisposable(String id, boolean status)
    {
        OutputData cfg = getConfig(id);
        cfg.setDisposable(status);
        
        return this;
    }

    @Override
    public OutputConfig createConfig()
    {
        String[] ids = new String[cfgs.size()];
        boolean[] systemOut = new boolean[ids.length];
        boolean[] disposableStatus = new boolean[ids.length];
        FileConfig[][] fileConfigs = new FileConfig[ids.length][];
        int i = 0;
        
        for(String id : cfgs.keySet())
        {
            OutputData cfg = cfgs.get(id);            
            ids[i] = id;
            systemOut[i] = cfg.useSystemOut();
            disposableStatus[i] = cfg.isDisposable();
            fileConfigs[i] = cfg.getFileConfigs();
            
            i++;
        }
        
        return new OutputConfigImpl(ids, systemOut, disposableStatus, fileConfigs);
    }

    @Override
    public void reset()
    {
        cfgs.clear();
    }
}