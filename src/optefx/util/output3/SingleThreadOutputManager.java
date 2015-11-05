
package optefx.util.output3;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * The single-threaded manager implementation, which operates with a simple 
 * id table for writers objects.
 * 
 * This implementation is best suited for production environments.
 * @author Enrique Urra C.
 */
class SingleThreadOutputManager extends BaseOutputManager
{
    /**
     * A table that relates the registered ids with output writers.
     */
    protected Map<String, HelperWriter> writersTable;

    /**
     * Default constructor.
     */
    public SingleThreadOutputManager()
    {
        this(new HashMap<String, HelperWriter>());
    }
    
    /**
     * Constructor that inits the manager with a predefined writer table.
     * @param startMap The map that stores the predefined writers.
     */
    public SingleThreadOutputManager(Map<String, HelperWriter> startMap)
    {
        super(startMap);
        writersTable = startMap;
    }
    
    /**
     * Get the current writers.
     * @return The writers as a map.
     */
    public Map<String, HelperWriter> getCurrentMap()
    {
        return writersTable;
    }

    @Override
    public void setOutputsFromConfig(OutputConfig config)
    {
        setOutputsFromConfig(config, null);
    }
    
    @Override
    public void setOutputsFromConfig(OutputConfig config, Writer systemWriter)
    {
        setOutputsFromConfig(config, systemWriter, writersTable);
    }

    @Override
    public PrintWriter getOutput(String id)
    {
        return getOutput(id, writersTable);
    }

    @Override
    public void closeOutputs()
    {        
        closeOutputs(writersTable);
    }

    @Override
    public void closeOutputs(String id)
    {
        closeOutputs(id, writersTable);
    }

    @Override
    public void clearOutputs()
    {
        clearOutputs(writersTable);
    }

    @Override
    public void clearOutputs(String id)
    {
        clearOutputs(id, writersTable);
    }
}