
package optefx.util.output3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Implements a custom writer which can be redirected to a single system writer
 * and to multiple file writers. Each writer operation perform the redirection
 * to the encapsulated writers.
 * @author Enrique Urra C.
 */
class HelperWriter extends Writer
{
    /**
     * The system writer to redirect.
     */
    private Writer systemWriter;
    /**
     * The file writers to redirect.
     */
    private FileWriter[] fileWriters;
    /**
     * Boolean that store the disposable status of the writer.
     */
    private boolean disposable;

    /**
     * Changes the disposable status of the writer.
     * @param disposable True to enable de status, false to disable.
     */
    public void setDisposable(boolean disposable)
    {
        this.disposable = disposable;
    }
    
    /**
     * Gets the disposable status of the writer.
     * @return True if the writer is disposable, false otherwise.
     */
    public boolean isDisposable()
    {
        return disposable;
    }
    
    /**
     * Sets the system writer.
     * @param systemWriter The writer object.
     */
    public void setSystemWriter(Writer systemWriter)
    {
        this.systemWriter = systemWriter;
    }

    /**
     * Sets all the file writer.
     * @param writers The file writers set as an array.
     */
    public void setFileWriters(FileWriter[] writers)
    {
        fileWriters = writers;
    }
    
    @Override
    public void write(char[] chars, int i, int i1) throws IOException
    {
        if(systemWriter != null)
        {
            systemWriter.write(chars, i, i1);
            systemWriter.flush();
        }
        
        if(fileWriters != null)
        {
            int fileCount = fileWriters.length;
        
            for(int k = 0; k < fileCount; k++)
            {
                fileWriters[k].write(chars, k, i1);
                fileWriters[k].flush();
            }
        }
    }

    @Override
    public void flush() throws IOException
    {
        if(systemWriter != null)
            systemWriter.flush();
        
        if(fileWriters != null)
        {
            int fileCount = fileWriters.length;
        
            for(int k = 0; k < fileCount; k++)
                fileWriters[k].flush();
        }
    }

    @Override
    public void close() throws IOException
    {
        if(fileWriters != null)
        {
            int fileCount = fileWriters.length;
        
            for(int k = 0; k < fileCount; k++)
                fileWriters[k].close();
        }
    }
}
