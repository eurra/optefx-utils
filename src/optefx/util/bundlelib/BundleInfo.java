
package optefx.util.bundlelib;

import java.util.jar.Attributes;

/**
 *
 * @author Enrique Urra C.
 */
public final class BundleInfo
{
    private final String pkg;
    private final String title;
    private final String version;
    private final String description;
    private final String jarPath;
    private final Attributes manifestData;

    public BundleInfo(String pkg, String title, String version, String description, String jarPath, Attributes manifestData)
    {
        this.pkg = pkg;
        this.title = title;
        this.version = version;
        this.description = description;
        this.jarPath = jarPath;
        this.manifestData = manifestData;
    }

    public String getPackage()
    {
        return pkg;
    }
    
    public String getTitle()
    {
        return title;
    }

    public String getVersion()
    {
        return version;
    }

    public String getDescription()
    {
        return description;
    }

    public String getJARPath()
    {
        return jarPath;
    }

    public Attributes getManifestData()
    {
        return manifestData;
    }
}
