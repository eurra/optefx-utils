
package optefx.util.output;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The default configuration builder implementation.
 * @author Enrique Urra C.
 */
public class DefaultOutputConfigBuilder implements OutputConfigBuilder, SystemOutputConfigBuilder
{
    private static class DefaultOutputConfig implements OutputConfig
    {
        private Map<String, OutputConfigEntry> entries;

        public DefaultOutputConfig(Map<String, OutputConfigEntry> entries)
        {
            this.entries = entries;
        }

        @Override
        public int getIdsCount()
        {
            return entries.size();
        }
        
        @Override
        public String[] getIds()
        {
            return entries.keySet().toArray(new String[0]);
        }
        
        @Override
        public boolean isPermanent(String id)
        {
            OutputConfigEntry entry = entries.get(id);
            
            if(entry == null)
                throw new NoSuchElementException("The id '" + id + "' has not been added to this configuration");
            
            return entry.isPermanent();
        }

        @Override
        public WriterBuilder[] getBuildersFor(String id)
        {
            OutputConfigEntry entry = entries.get(id);
            
            if(entry == null)
                throw new NoSuchElementException("The id '" + id + "' has not been added to this configuration");
            
            return entry.getBuilders();
        }
    }
    
    private static class OutputConfigEntry
    {
        private List<WriterBuilder> builders;
        private boolean permanent;

        public OutputConfigEntry()
        {
            this.builders = new ArrayList<>();
        }

        public void makePermanent()
        {
            this.permanent = true;
        }

        public boolean isPermanent()
        {
            return permanent;
        }
        
        public void addBuilder(WriterBuilder builder)
        {
            builders.add(builder);
        }
        
        public WriterBuilder[] getBuilders()
        {
            return builders.toArray(new WriterBuilder[0]);
        }
    }
    
    private class SystemWriterBuilder implements WriterBuilder
    {
        @Override
        public Writer build()
        {
            return systemOutput;
        }
    }
    
    private class SystemErrorWriterBuilder implements WriterBuilder
    {
        @Override
        public Writer build()
        {
            return systemErrorOutput;
        }
    }
    
    private Map<String, OutputConfigEntry> entries;
    private Writer systemOutput = new PrintWriter(System.out);
    private Writer systemErrorOutput = new PrintWriter(System.err);

    public DefaultOutputConfigBuilder()
    {
        this.entries = new HashMap<>();
    }
    
    @Override
    public DefaultOutputConfigBuilder setSystemOutput(Writer output)
    {
        if(output != null)
            systemOutput = output;
        
        return this;
    }
    
    @Override
    public DefaultOutputConfigBuilder setSystemOutput(OutputStream output)
    {
        if(output != null)
            systemOutput = new PrintWriter(output);
        
        return this;
    }
    
    @Override
    public DefaultOutputConfigBuilder setSystemErrorOutput(Writer output)
    {
        if(output != null)
            systemErrorOutput = output;
        
        return this;
    }
    
    @Override
    public DefaultOutputConfigBuilder setSystemErrorOutput(OutputStream output)
    {
        if(output != null)
            systemErrorOutput = new PrintWriter(output);
        
        return this;
    }
    
    public DefaultOutputConfigBuilder addSystemOutput(String id)
    {
        return addOutput(id, new SystemWriterBuilder());
    }
    
    public DefaultOutputConfigBuilder addSystemErrorOutput(String id)
    {
        return addOutput(id, new SystemErrorWriterBuilder());
    }
    
    public DefaultOutputConfigBuilder addFileOutput(String id, String filePath)
    {
        return addFileOutput(id, filePath, true);
    }
    
    public DefaultOutputConfigBuilder addFileOutput(String id, String filePath, boolean append)
    {
        return addOutput(id, new FileWriterBuilder(filePath).enableAppend(append));
    }
    
    @Override
    public DefaultOutputConfigBuilder addOutput(String id, WriterBuilder builder)
    {
        if(id != null && builder != null)
        {
            OutputConfigEntry entry = entries.get(id);
        
            if(entry == null)
            {
                entry = new OutputConfigEntry();
                entries.put(id, entry);
            }
            
            entry.addBuilder(builder);
        }
        
        return this;
    }
    
    @Override
    public DefaultOutputConfigBuilder makePermanent(String id)
    {
        if(id != null)
        {
            OutputConfigEntry entry = entries.get(id);
            
            if(entry != null)
                entry.makePermanent();
        }
        
        return this;
    }

    @Override
    public OutputConfig build()
    {
        return new DefaultOutputConfig(entries);
    }
}