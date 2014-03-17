
package optefx.util.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Enrique Urra C.
 */
public class FileWriterBuilder implements WriterBuilder
{
    private String path;
    private boolean append;

    public FileWriterBuilder(String path)
    {
        if(path == null)
            throw new NullPointerException("Null path");
        
        this.path = path;
    }
    
    public FileWriterBuilder enableAppend(boolean enable)
    {
        this.append = enable;
        return this;
    }

    @Override
    public Writer build() throws IOException
    {
        File file = new File(path);
        File dir = file.getParentFile();
        dir.mkdirs();
        
        if(!dir.exists())
            throw new IOException("Cannot generate directories for the path '" + path + "'");
        
        FileWriter fileWriter = null;
        
        try
        {
            fileWriter = new FileWriter(path, append);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Error while creating output file.", ex);
        }
        
        return fileWriter;
    }
}
