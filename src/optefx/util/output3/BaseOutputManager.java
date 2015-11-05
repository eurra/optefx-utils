
package optefx.util.output3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
abstract class BaseOutputManager extends OutputManager
{
    /**
     * A table which store the PrintWriter wrappers created for each registered
     * output. Useful for avoid constant PrintWriter creation.
     */
    protected Map<HelperWriter, PrintWriter> wrappers;
    
    /**
     * Default constructor that use a map of writers by default.
     * @param table The map of writers.
     */
    public BaseOutputManager(Map<String, HelperWriter> table)
    {
        wrappers = new HashMap<>();
        
        for(HelperWriter writer : table.values())
            addWrapper(writer);
    }
    
    /**
     * Adds a new print wrapper to the manager.
     * @param writer The writer to wrap.
     */
    private void addWrapper(HelperWriter writer)
    {
        PrintWriter pw = new PrintWriter(writer);
        wrappers.put(writer, pw);
    }
    
    /**
     * For the specified table, retrieves or initializes a writer based on the
     * specified id.
     * @param id The id of the output.
     * @param table The table in which the is is searched.
     * @return The new or registered writer.
     */
    private HelperWriter getAlgorithmWriter(String id, Map<String, HelperWriter> table)
    {
        HelperWriter writer = table.get(id);
            
        if(writer == null)
        {
            writer = new HelperWriter();
            addWrapper(writer);
            table.put(id, writer);
        }
        
        return writer;
    }
    
    /**
     * A method wrapper for the OutputManager.setOutputsFromConfig()
     *  method.
     * @param config The configuration object.
     * @param systemWriter A custom writer to be used as the system output.
     * @param table The table in which the is is searched.
     */
    public void setOutputsFromConfig(OutputConfig config, Writer systemWriter, Map<String, HelperWriter> table)
    {
        int idCount = config.getIdsCount();
        
        for(int i = 0; i < idCount; i++)
        {
            String id = config.getId(i);
            HelperWriter writer = getAlgorithmWriter(id, table);
            
            if(config.idUsesSystemOut(i))
                writer.setSystemWriter(systemWriter == null ? new OutputStreamWriter(System.out) : systemWriter);
            
            if(config.isIdDisposable(i))
                writer.setDisposable(true);
            
            int filesCount = config.getIdFileOutputCount(i);
            FileWriter[] fileWriters = new FileWriter[filesCount];
            
            for(int j = 0; j < filesCount; j++)
            {
                String filePath = config.getIdFileOutputPath(i, j);
                boolean append = config.getIdFileOutputAppend(i, j);

                File file = new File(filePath);
                file.getParentFile().mkdirs();            
                FileWriter fileWriter = null;

                try
                {
                    fileWriter = new FileWriter(filePath, append);
                }
                catch(IOException ex)
                {
                    throw new RuntimeException("Error while creating output file.", ex);
                }
                
                fileWriters[j] = fileWriter;
            }
            
            writer.setFileWriters(fileWriters);
        }
    }
    
    /**
     * A method wrapper for the OutputManager.getOutput() method.
     * @param id The id of the output to print.
     * @param table The table in which the is is searched.
     * @return The writer as a PrintWriter object.
     */
    public PrintWriter getOutput(String id, Map<String, HelperWriter> table)
    {
        HelperWriter writer = table.get(id);
        
        if(writer == null)
            return null;
        
        PrintWriter pw = wrappers.get(writer);
        return pw;
    }
    
    /**
     * A method wrapper for the OutputManager.closeOutputs() method
     * @param table The table in which the is is searched.
     */
    public void closeOutputs(Map<String, HelperWriter> table)
    {        
        for(String id : table.keySet())
            closeOutputs(id, table);
    }
    
    /**
     * A method wrapper for the OutputManager.closeOutputs() method
     * @param id The id of the outputs to finalize.
     * @param table The table in which the is is searched.
     */
    public void closeOutputs(String id, Map<String, HelperWriter> table)
    {
        HelperWriter writer = table.get(id);
        
        try
        {
            if(writer != null)
                writer.close();
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Error while closing output file.", ex);
        }
    }

    /**
     * A method wrapper for the OutputManager.clearOutputs() method
     * @param table The table in which the is is searched.
     */
    public void clearOutputs(Map<String, HelperWriter> table)
    {
        ArrayList<String> idsToClear = new ArrayList<>();
        
        for(String id : table.keySet())
        {
            if(table.get(id).isDisposable())
                idsToClear.add(id);
        }
        
        int count = idsToClear.size();
        
        for(int i = 0; i < count; i++)
            table.remove(idsToClear.get(i));
    }
    
    /**
     * A method wrapper for the OutputManager.clearOutputs() method
     * @param id The id of the outputs to finalize.
     * @param table The table in which the is is searched.
     */
    public void clearOutputs(String id, Map<String, HelperWriter> table)
    {
        HelperWriter writer = table.get(id);
        
        if(writer != null && writer.isDisposable())        
            table.remove(id);
    }
}
