
package optefx.util.output3;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The multi-threaded manager implementation, which operates based on the 
 * current thread, i.e., the same id can be used in two different threads and
 * two different outputs will be generated, one for each thread. If some 
 * algorithm uses an id to print output, the default manager will search the
 * algorithm's thread for outputs registered with such id.
 * 
 * This implementation can cause an overhead because the thread accessing, 
 * therefore  it should be used for development environments only.
 * @author Enrique Urra C.
 */
class MultiThreadOutputManager extends BaseOutputManager
{
    /**
     * A table that relates the registered threads with output writers.
     */
    private Map<Thread, Map<String, HelperWriter>> threadsTable;

    /**
     * Default constructor.
     */
    public MultiThreadOutputManager()
    {
        this(new HashMap<String, HelperWriter>());
    }
    
    /**
     * Constructor that inits the manager with a predefined writer table, 
     * relating it with the current thread.
     * @param startMap The map that stores the predefined writers.
     */
    public MultiThreadOutputManager(Map<String, HelperWriter> startMap)
    {
        super(startMap);
        threadsTable = new HashMap<>();
        threadsTable.put(Thread.currentThread(), startMap);
    }
    
    /**
     * Get the writers of the current thread.
     * @return The writers as a map.
     */
    public Map<String, HelperWriter> getCurrentMap()
    {
        return getThreadTable();
    }
    
    /**
     * Gets or creates the output table associated with a particular thread.
     * @return The table which relate output ids with writers for the current
     *  thread.
     */
    private Map<String, HelperWriter> getThreadTable()
    {
        Thread currThread = Thread.currentThread();
        Map<String, HelperWriter> table = threadsTable.get(currThread);
        
        if(table == null)
        {
            table = new HashMap<>();
            threadsTable.put(currThread, table);
        }
        
        return table;
    }
    
    /**
     * Performs a cleanup of the outputs registered with currently dead threads.
     * This method is called each time a new set of outputs is registered.
     */
    private void cleanUp()
    {
        ArrayList<Thread> deadThreads = new ArrayList<>(threadsTable.size());
        
        for(Thread thread : threadsTable.keySet())
        {
            if(!thread.isAlive())
                deadThreads.add(thread);
        }
        
        int count = deadThreads.size();
        
        for(int i = 0; i < count; i++)
        {
            Thread thread = deadThreads.get(i);
            Map<String, HelperWriter> table = threadsTable.get(thread);
            
            for(HelperWriter writer : table.values())
                wrappers.remove(writer);
            
            threadsTable.remove(thread);
        }
    }

    @Override
    public void setOutputsFromConfig(OutputConfig config)
    {
        setOutputsFromConfig(config, null);
    }
    
    @Override
    public void setOutputsFromConfig(OutputConfig config, Writer systemWriter)
    {
        setOutputsFromConfig(config, systemWriter, getThreadTable());        
        cleanUp();
    }

    @Override
    public PrintWriter getOutput(String id)
    {
        return getOutput(id, getThreadTable());
    }

    @Override
    public void closeOutputs()
    {
        closeOutputs(getThreadTable());
    }

    @Override
    public void closeOutputs(String id)
    {
        closeOutputs(id, getThreadTable());
    }

    @Override
    public void clearOutputs()
    {
        clearOutputs(getThreadTable());
    }

    @Override
    public void clearOutputs(String id)
    {
        clearOutputs(id, getThreadTable());
    }
}