
package optefx.util.output3;

/**
 * Defines a builder for output configurations, which can be used by an output
 * manager. An individual builder can enable the system output of multiple 
 * identifiers and can add multiple file outputs to multiple identifiers.
 * @author Enrique Urra C.
 */
public interface OutputConfigBuilder
{
    /**
     * Enables the system output of the specified output identifier.
     * @param id The id of the output.
     * @return The current builder instance.
     */
    OutputConfigBuilder enableSystemOutput(String id);
    
    /**
     * Adds a new file output to the specified output identifier.
     * @param id The id of the output.
     * @param file The file path of the output.
     * @param append Enables o disables the appending on the specified file.
     * @return The current builder instance.
     */
    OutputConfigBuilder addFileOutput(String id, String file, boolean append);
    
    /**
     * Enables or disables the disposable status of the output related to the
     * provided identifier. If the status is disabled, further calls to the
     * reset() method will not remove the output.
     * @param id The id for which the status will be setted
     * @param status true for enabling the disposable status, false for disabling.
     * @return The current builder instance.
     */
    OutputConfigBuilder setDisposable(String id, boolean status);
    
    /**
     * Creates the configuration based on the current builder state.
     * @return The configuration object.
     */
    OutputConfig createConfig();
    
    /**
     * Resets the builder state, cleaning all ids and their related outputs, 
     * excepting the ones with the disposable status disabled.
     */
    void reset();
}