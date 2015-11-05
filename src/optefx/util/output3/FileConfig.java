
package optefx.util.output3;

/**
 * Implements a bean that store file output information.
 * @author Enrique Urra C.
 */
class FileConfig
{
    private String path;
    private boolean append;

    public FileConfig(String path, boolean append)
    {
        this.path = path;
        this.append = append;
    }

    public String getPath()
    {
        return path;
    }

    public boolean getAppend()
    {
        return append;
    }
}
