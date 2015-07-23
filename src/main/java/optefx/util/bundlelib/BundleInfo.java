
package optefx.util.bundlelib;

/**
 *
 * @author Enrique Urra C.
 */
public final class BundleInfo
{
    private final String pkg;
    private final String title;
    private final String version;
    private final String vendor;
    private final String jarPath;

    public BundleInfo(String pkg, String title, String version, String vendor, String jarPath)
    {
        this.pkg = pkg;
        this.title = title;
        this.version = version;
        this.vendor = vendor;
        this.jarPath = jarPath;
    }

    public String getPackage()
    {
        return pkg;
    }
    
    public String getTitle()
    {
        return title != null ? title : "(no title)";
    }

    public String getVersion()
    {
        return version != null ? version : "(no version)";
    }

    public String getVendor()
    {
        return vendor != null ? vendor : "(no vendor)";
    }

    public String getJARPath()
    {
        return jarPath;
    }
}
