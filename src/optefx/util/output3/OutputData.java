
package optefx.util.output3;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a bean that stores an output id configuration.
 * @author Enrique Urra C.
 */
class OutputData
{
    private boolean useSystemOut;
    private boolean disposable;
    private List<FileConfig> fileCfgs;

    public OutputData()
    {
        this.fileCfgs = new ArrayList<>();
        this.disposable = true; // disposable by default.
    }
    
    public void setSystemOut(boolean useSystemOut)
    {
        this.useSystemOut = useSystemOut;
    }
    
    public boolean useSystemOut()
    {
        return useSystemOut;
    }
    
    public void setDisposable(boolean disposable)
    {
        this.disposable = disposable;
    }
    
    public boolean isDisposable()
    {
        return disposable;
    }
    
    public void addFileConfig(FileConfig config)
    {
        fileCfgs.add(config);
    }
    
    public FileConfig[] getFileConfigs()
    {
        int count = fileCfgs.size();
        FileConfig[] ret = new FileConfig[fileCfgs.size()];
        
        for(int i = 0; i < count; i++)
        {
            FileConfig currCfg = fileCfgs.get(i);
            ret[i] = new FileConfig(currCfg.getPath(), currCfg.getAppend());
        }
        
        return ret;
    }
}