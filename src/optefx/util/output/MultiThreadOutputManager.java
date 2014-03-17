
package optefx.util.output;

import java.io.PrintWriter;
import java.util.WeakHashMap;

public class MultiThreadOutputManager extends OutputManager
{
    private WeakHashMap<Thread, OutputManager> threadsMap;

    public MultiThreadOutputManager()
    {
        this.threadsMap = new WeakHashMap<>();
    }
    
    @Override
    public final void init()
    {
        threadsMap.clear();
        addManager(Thread.currentThread(), OutputManager.getBaseManager());
    }
    
    private void addManager(Thread thread, OutputManager manager)
    {
        threadsMap.put(thread, manager);
    }
    
    private OutputManager getManagerForThread()
    {
        Thread currThread = Thread.currentThread();
        OutputManager manager = threadsMap.get(currThread);
        
        if(manager == null)
        {
            manager = new DefaultOutputManager();
            addManager(currThread, manager);
        }
        
        return manager;
    }

    @Override
    public void setOutputsFromConfig(OutputConfig config)
    {
        getManagerForThread().setOutputsFromConfig(config);
    }

    @Override
    public PrintWriter getOutput(String id)
    {
        return getManagerForThread().getOutput(id);
    }

    @Override
    public void closeOutputs()
    {
        getManagerForThread().closeOutputs();
    }

    @Override
    public void closeOutputs(String id)
    {
        getManagerForThread().clearOutputs(id);
    }

    @Override
    public void clearOutputs()
    {
        getManagerForThread().clearOutputs();
    }

    @Override
    public void clearOutputs(String id)
    {
        getManagerForThread().clearOutputs(id);
    }
}