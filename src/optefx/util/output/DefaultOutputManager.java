
package optefx.util.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultOutputManager extends OutputManager
{  
    private class MultiWriter extends Writer
    {
        private Writer[] writers = new Writer[0];
        private boolean permanent;

        public void setPermanent()
        {
            this.permanent = true;
        }

        public boolean isPermanent()
        {
            return permanent;
        }

        public void setWriters(Writer[] writers)
        {
            if(writers == null)
                throw new NullPointerException("Null writers array");

            Writer[] newWriters = new Writer[writers.length];

            for(int i = 0; i < writers.length; i++)
            {
                if(writers[i] == null)
                   throw new NullPointerException("Null writer at position " + i);

                newWriters[i] = writers[i];
            }

            this.writers = newWriters;
        }

        @Override
        public void write(char[] chars, int i, int i1) throws IOException
        {
            for(int j = 0; j < writers.length; j++)
            {
                writers[j].write(chars, i, i1);
                writers[j].flush();
            }
        }

        @Override
        public void flush() throws IOException
        {
            for(int i = 0; i < writers.length; i++)
                writers[i].flush();
        }

        @Override
        public void close() throws IOException
        {
            if(permanent)
                return;

            for(int i = 0; i < writers.length; i++)
                writers[i].close();
        }
    }
    
    private Map<String, MultiWriter> table;
    private Map<MultiWriter, PrintWriter> wrappers;

    public DefaultOutputManager()
    {
        this.table = new HashMap<>();
        this.wrappers = new HashMap<>();
    }

    @Override
    public void init()
    {
    }
    
    private void addWrapper(MultiWriter writer)
    {
        PrintWriter pw = new PrintWriter(writer);
        wrappers.put(writer, pw);
    }
    
    private MultiWriter getWriter(String id)
    {
        MultiWriter writer = table.get(id);
            
        if(writer == null)
        {
            writer = new MultiWriter();
            addWrapper(writer);
            table.put(id, writer);
        }
        
        return writer;
    }
    
    @Override
    public void setOutputsFromConfig(OutputConfig config)
    {
        String[] ids = config.getIds();
        
        for(int i = 0; i < ids.length; i++)
        {
            String currId = ids[i];
            MultiWriter writer = getWriter(ids[i]);
            
            if(config.isPermanent(currId))
                writer.setPermanent();
            
            WriterBuilder[] builders = config.getBuildersFor(currId);
            Writer[] finalWriters = new Writer[builders.length];
            WriterBuilder currBuilder = null;
            
            try
            {
                for(int j = 0; j < builders.length; j++)
                {
                    currBuilder = builders[j];
                    finalWriters[j] = currBuilder.build();
                }
            }
            catch(IOException ex)
            {
                throw new RuntimeException("Cannot build an instance through a '" + currBuilder.getClass() + "' builder instance", ex);
            }
            
            writer.setWriters(finalWriters);
        }
    }
    
    @Override
    public PrintWriter getOutput(String id)
    {
        MultiWriter writer = table.get(id);
        
        if(writer == null)
            return null;
        
        PrintWriter pw = wrappers.get(writer);
        return pw;
    }
    
    @Override
    public void closeOutputs()
    {        
        for(String id : table.keySet())
            closeOutputs(id);
    }
    
    @Override
    public void closeOutputs(String id)
    {
        MultiWriter writer = table.get(id);
        
        try
        {
            if(writer != null)
                writer.close();
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Error while closing the '" + id + "' output", ex);
        }
    }

    @Override
    public void clearOutputs()
    {
        ArrayList<String> idsToClear = new ArrayList<>();
        
        for(String id : table.keySet())
        {
            if(!table.get(id).isPermanent())
                idsToClear.add(id);
        }
        
        int count = idsToClear.size();
        
        for(int i = 0; i < count; i++)
        {
            MultiWriter writer = table.remove(idsToClear.get(i));
            wrappers.remove(writer);
        }
    }
    
    @Override
    public void clearOutputs(String id)
    {
        MultiWriter writer = table.get(id);
        
        if(writer != null && !writer.isPermanent())
        {
            table.remove(id);
            wrappers.remove(writer);
        }
    }
}
