
package optefx.util.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The default configuration builder implementation.
 * @author Enrique Urra C.
 */
public class OutputConfigBuilder
{
    private static class DefaultOutputConfig implements OutputConfig
    {
        private final Map<String, OutputConfigEntry> entries;

        public DefaultOutputConfig(Map<String, OutputConfigEntry> entries)
        {
            this.entries = new HashMap<>(entries);
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
        public boolean isPersistent(String id)
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
        private final List<WriterBuilder> builders;
        private boolean permanent;

        public OutputConfigEntry()
        {
            this.builders = new ArrayList<>();
        }

        public void makePersistent()
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
    
    private static class UnclosableWriter extends Writer
    {
        private final Writer innerWritter;
        
        public UnclosableWriter(Writer innerWritter)
        {
            this.innerWritter = innerWritter;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException
        {
            innerWritter.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException
        {
            innerWritter.flush();
        }

        @Override
        public void close() throws IOException
        {
        }
    }
    
    private final Map<String, OutputConfigEntry> entries;
    private final Set<String> systemEntries;
    private final Set<String> systemErrorEntries;
    private Writer systemOutput = new UnclosableWriter(new PrintWriter(System.out));
    private Writer systemErrorOutput = new UnclosableWriter(new PrintWriter(System.err));

    public OutputConfigBuilder()
    {
        this.entries = new HashMap<>();
        this.systemEntries = new HashSet<>();
        this.systemErrorEntries = new HashSet<>();
    }
    
    public OutputConfigBuilder setSystemOutput(Writer output)
    {
        if(output != null)
            systemOutput = output;
        
        return this;
    }
    
    public OutputConfigBuilder setSystemOutput(OutputStream output)
    {
        if(output != null)
            systemOutput = new PrintWriter(output);
        
        return this;
    }
    
    public OutputConfigBuilder setSystemErrorOutput(Writer output)
    {
        if(output != null)
            systemErrorOutput = output;
        
        return this;
    }
    
    public OutputConfigBuilder setSystemErrorOutput(OutputStream output)
    {
        if(output != null)
            systemErrorOutput = new PrintWriter(output);
        
        return this;
    }
    
    public OutputConfigBuilder addSystemOutputId(String id)
    {
        systemEntries.add(id);
        return this;
    }
    
    public OutputConfigBuilder addSystemErrorOutputId(String id)
    {
        systemErrorEntries.add(id);
        return this;
    }
    
    public OutputConfigBuilder addFileOutput(String id, String filePath)
    {
        return addFileOutput(id, filePath, true);
    }
    
    public OutputConfigBuilder addFileOutput(String id, String filePath, boolean append)
    {
        return addOutput(id, new FileWriterBuilder(filePath).enableAppend(append));
    }
    
    private OutputConfigEntry getEntry(String id)
    {
        OutputConfigEntry entry = entries.get(id);
        
        if(entry == null)
        {
            entry = new OutputConfigEntry();
            entries.put(id, entry);
        }
        
        return entry;
    }
    
    public OutputConfigBuilder addOutput(String id, WriterBuilder builder)
    {
        if(id != null && builder != null)
            getEntry(id).addBuilder(builder);
        
        return this;
    }
    
    public OutputConfigBuilder makePersistent(String id)
    {
        if(id != null)
            getEntry(id).makePersistent();
        
        return this;
    }

    public OutputConfig build()
    {
        WrapperWriterBuilder systemBuilder = new WrapperWriterBuilder(systemOutput);
        WrapperWriterBuilder systemErrorBuilder = new WrapperWriterBuilder(systemErrorOutput);
        
        for(String systemId : systemEntries)
            addOutput(systemId, systemBuilder);
        
        for(String systemErrorId : systemErrorEntries)
            addOutput(systemErrorId, systemErrorBuilder);
        
        return new DefaultOutputConfig(entries);
    }
}