
package optefx.util.output3;

/**
 * Defines a configuration structure for algorithm outputs. This configuration
 * can hold multiple output identifiers, and each identifier may have enabled 
 * its system output and may have multiple file outputs associated.
 * @author Enrique Urra C.
 */
public interface OutputConfig
{
    /**
     * Gets the count of registered ids within this config.
     * @return the count as int.
     */
    int getIdsCount();
    
    /**
     * Gets the id in the specified index.
     * @param idIndex The index of the id.
     * @return The id as String.
     */
    String getId(int idIndex);
    
    /**
     * Returns a boolean that indicates if the id at the specified index has 
     * enabled the system output.
     * @param idIndex The index of the id.
     * @return true if the id has enabled the system output, false otherwise.
     */
    boolean idUsesSystemOut(int idIndex);
    
    /**
     * Gets the disposable status of the id at the specified index.
     * @param idIndex The index of the id.
     * @return true if the id is disposable, false otherwise.
     */
    boolean isIdDisposable(int idIndex);
    
    /**
     * Gets the count of registered file outputs of the id at the specified 
     * index within this config.
     * @param idIndex The index of the id.
     * @return the count as int.
     */
    int getIdFileOutputCount(int idIndex);
    
    /**
     * Gets the file path related to the output of the id at the specified 
     * index.
     * @param idIndex The index of the id.
     * @param outputIndex The index of file output for the specified id.
     * @return The path as String.
     */
    String getIdFileOutputPath(int idIndex, int outputIndex);
    
    /**
     * Checks the append option related to the output of the id at the specified 
     * index.
     * @param idIndex The index of the id.
     * @param outputIndex The index of file output for the specified id.
     * @return true if the append option is enabled, false otherwise.
     */
    boolean getIdFileOutputAppend(int idIndex, int outputIndex);
}