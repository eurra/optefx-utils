
package optefx.util.output;

/**
 * Defines a configuration structure for outputs. This configuration
 * can hold multiple output identifiers, and each identifier may have multiple 
 * writer outputs associated.
 * @author Enrique Urra C.
 */
public interface OutputConfig
{
    /**
     * Gets the count of registered ids within this config.
     * @return the count as int.
     */
    int getIdsCount();
    String[] getIds();    
    WriterBuilder[] getBuildersFor(String id);
    boolean isPermanent(String id);
}