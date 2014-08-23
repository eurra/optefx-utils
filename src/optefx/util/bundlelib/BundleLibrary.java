
package optefx.util.bundlelib;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 * @author Enrique Urra C.
 */
public final class BundleLibrary
{
    public static BundleLibrary getFromClassLoader(ClassLoader loader) throws BundleException
    {
        List<BundleInfo> bundles = new ArrayList<>();
        
        try
        {
            Enumeration<URL> resources = loader.getResources("META-INF/MANIFEST.MF");
            
            while(resources.hasMoreElements())
            {
                URL resource = resources.nextElement();
                Manifest manifest = new Manifest(resource.openStream());
                loadBundlesFromManifest(manifest, new File(resource.getFile()), bundles);
            }
        }
        catch(IOException ex)
        {
            throw new BundleException("Cannot load scripts bundles from classpath: " + ex.getLocalizedMessage(), ex);
        }
        
        return new BundleLibrary(BundleLibrary.class.getClassLoader(), bundles.toArray(new BundleInfo[0]));
    }
    
    public static BundleLibrary getFromPaths(String... paths) throws BundleException
    {
        return getFromPaths(true, paths);
    }
    
    public static BundleLibrary getFromPaths(boolean searchRecursively, String... paths) throws BundleException
    {
        Set<URL> filesURLs = new HashSet<>();
        List<BundleInfo> bundles = new ArrayList<>();
        
        for (String path : paths)
            loadBundlesFromPath(path, bundles, filesURLs, new HashSet<>(), searchRecursively);

        ClassLoader loader = URLClassLoader.newInstance(filesURLs.toArray(new URL[0]));
        return new BundleLibrary(loader, bundles.toArray(new BundleInfo[0]));
    }
    
    private static void loadBundlesFromPath(String path, List<BundleInfo> bundles, Set<URL> filesURLs, Set<String> checkedPaths, boolean searchRecursively)
    {
        File folder = new File(path);
        
        if(checkedPaths.contains(path))
            return;
        
        if(!folder.exists() || !folder.isDirectory())
           folder.mkdirs();
        
        File[] filesInFolder = folder.listFiles();
        List<String> subFolders = new ArrayList<>();
        
        for (File toCheck : filesInFolder)
        {
            if(fileIsJar(toCheck))
            {   
                Manifest manifest;

                try
                {
                    JarFile currJarFile = new JarFile(toCheck);
                    manifest = currJarFile.getManifest();
                }
                catch(IOException ex)
                {
                    throw new BundleException("Cannot load jar file '" + toCheck + "': " + ex.getLocalizedMessage(), ex);
                }

                if(manifest != null && loadBundlesFromManifest(manifest, toCheck, bundles))
                {
                    try
                    {
                        filesURLs.add(toCheck.toURI().toURL());
                    }
                    catch(MalformedURLException ex)
                    {
                        throw new BundleException("Cannot load script bundles from jar: " + ex.getLocalizedMessage(), ex);
                    }
                }
            }
            else if(toCheck.isDirectory())
            {
                subFolders.add(toCheck.getPath());
            }
        }
        
        checkedPaths.add(path);
        
        if(searchRecursively)
        {
            int subFoldersCount = subFolders.size();
        
            for(int i = 0; i < subFoldersCount; i++)
                loadBundlesFromPath(subFolders.get(i), bundles, filesURLs, checkedPaths, true);
        }
    }
    
    private static boolean fileIsJar(File file)
    {
        if(!file.isFile())
            return false;
        
        String name = file.getName().toLowerCase();        
        return name.endsWith(".jar") || name.endsWith(".zip");
    }
    
    private static boolean loadBundlesFromManifest(Manifest manifest, File jarFile, List<BundleInfo> bundles)
    {
        Map<String, Attributes> jarBundles = manifest.getEntries();
        boolean bundleLoaded = false;

        for(String name : jarBundles.keySet())
        {
            Attributes attrs = jarBundles.get(name);
            String isBundleBool = attrs.getValue("Script-Bundle");

            if(isBundleBool == null || !Boolean.parseBoolean(isBundleBool))
                continue;

            String title = attrs.getValue("Script-Bundle-Title");
            String version = attrs.getValue("Script-Bundle-Version");
            String description = attrs.getValue("Script-Bundle-Description");

            BundleInfo bInfo = new BundleInfo(
                name.replaceAll("/", ".").substring(0, name.length() - 1),
                title,
                version,
                description,
                jarFile.getPath(),
                attrs
            );

            bundles.add(bInfo);
            
            if(!bundleLoaded)
                bundleLoaded = true;
        }
        
        return bundleLoaded;
    }
    
    private final ClassLoader loader;
    private final BundleInfo[] bundles;

    private BundleLibrary(ClassLoader loader, BundleInfo[] bundles)
    {        
        this.loader = loader;
        this.bundles = bundles;
    }

    public ClassLoader getLoader()
    {
        return loader;
    }
    
    public BundleInfo[] getBundles()
    {
        return Arrays.copyOf(bundles, bundles.length);
    }
    
    public final Class loadClass(String className) throws BundleException
    {        
        try
        {
            return (Class)loader.loadClass(className);
        }
        catch(ClassCastException ex)
        {
            throw new BundleException("The class '" + className + "' is not a valid script type", ex);
        }
        catch(ClassNotFoundException ex)
        {
            throw new BundleException("The script class '" + className + "' was not found", ex);
        }
    }
}