
package optefx.util.output3;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Defines an algorithm output manager, a helper class for different output
 * requirements in algorithms. The class itself works as a mix of singleton and
 * factory, providing the instances of default implementations.
 * 
 * Through this class, three different operations can be performed:
 * a) Configure a new set of output profiles by using specific configuration
 *    objects (see the 'setOutputsFromConfig' methods).
 * b) Get a reference of an output writer by using a particular id, which may
 *    be registered in a configuration previously performed (see the 
 *    'getOutput' method). An output writer corresponds to a PrintWriter 
 *    object.
 * c) Close the related file outputs that may be registered in a configuration 
 *    previously performed (see the 'closeOutputs' methods).
 * 
 * From the static context, other operations are available:
 * a) Get the unique instance (singleton implementation).
 * b) Create a configuration builder instance, which allows to create an object 
 *    to be used alongside the manager for output configurations.
 * c) Call common methods of PrintWriter objects associated with outputs 
 *    registered through the manager.
 * 
 * A common manager usage involves the following steps. First, a configuration 
 * builder must be created
 * 
 *      OutputConfigBuilder builder = OutputManager
 *                                              .createConfigBuilder();
 * 
 * The builder allows to configure different outputs through particular string
 * identifiers. Such ids can be used within algorithms to generate output 
 * information. A configuration object can be generated as a builder result.
 * For example, the following instructions create two different outputs, each
 * one associated with a particular id: "my_so_id" is intended to be a system
 * output (console output, windows output, etc.), and "my_file_id" is intended
 * to be a file output (in a new file "output_file.txt" to be created or 
 * appended):
 * 
 *      OutputConfigBuilder config1;
 *      config1 = builder.enableSystemOutput("my_so_id")
 *                  .addFileOutput("my_file_id", "output_file.txt", true)
 *                  .createConfig();
 * 
 * A single id can be used as a single system output and as multiple file 
 * outputs, therefore any usage of such id in algorithms should output the 
 * same text to the related system and file outputs, at the same time. The
 * following example shows how the same id can be used as a system output and
 * as two different file outputs:
 * 
 *      OutputConfigBuilder config2;
 *      config2 = builder.enableSystemOutput("same_output")
 *                  .addFileOutput("same_output", "output_file_01.txt", true)
 *                  .addFileOutput("same_output", "output_file_02.txt", true)
 *                  .createConfig();
 * 
 * Then, the configs can been used alongside the manager to create the outputs. 
 * As soon as the configs are processed by the manager, the outputs may be  
 * available for use:
 * 
 *      OutputManager.getInstance().setOutputsFromConfig(config1);
 *      OutputManager.getInstance().setOutputsFromConfig(config2);
 * 
 * Now, any algorithm can output information through the configured ids by using
 * the manager methods:
 * 
 *      OutputManager.getInstance().getOutput("my_so_id")
 *          .println("This will be printed in the console output.");
 * 
 *      OutputManager.getInstance().getOutput("my_file_id")
 *          .println("This will be printed in the file 'output_file.txt'.");
 * 
 *      OutputManager.println("my_so_id", "This call uses the static 
 *          manager accesor to print lines.");
 * 
 *      OutputManager.println("same_id", "This will be printed in the
 *          console output and in the files "output_file_01.txt" and 
 *          "output_file_02.txt" at the same time.");
 * 
 * If some algorithm uses an id to print output, the default manager will search
 * for outputs registered with such id. By default, if no output is found, the 
 * getOutput() call returns null. The static printing methods in the 
 * OutputManager class are null-pointer safe because they does nothing
 * when the getOutput() method returns null:
 * 
 *      OutputManager.println("a_no_registered_id", "This call is
 *          null-pointer safe. The 'a_no_registered_id' may not be configured
 *          previously and nothing will be printed.");
 * 
 * The outputs registered and active in the manager can be closed/terminated
 * in any moment. Such finalization is commonly called after the algorithm 
 * execution. Alternatively, outputs associated with particular ids can be
 * terminated independently:
 * 
 *      OutputManager.getInstance().closeOutputs("same_id");
 *      OutputManager.getInstance().closeOutputs();
 * 
 * The manner how the configured ids are handled depends on the manager 
 * implementation. By default, there are two different implementations in the
 * Manager:
 * - A single thread implementation, which allows to perform faster output 
 *   printing. As this implementation runs in a single thread, each id 
 *   registered within the manager will be associated to a single PrintWriter
 *   object.
 * - A multiple thread implementation, handles the ids based on the active 
 *   thread, i.e., the same id can be used in two different threads and two 
 *   different PrintWritter objects will be generated, one for each thread. 
 *   Because the thread management, this implementation is only recommended for 
 *   develop or required multi-thread environments.
 * 
 * Both implementations can be switched through the setMode() method.
 * 
 * @author Enrique Urra C.
 */
public abstract class OutputManager
{    
    /**
     * Single thread flag for output management.
     */
    public static final int MODE_SINGLE_THREAD = 0;
    /**
     * Multiple thread flag for output management.
     */
    public static final int MODE_MULTI_THREAD = 1;
    
    /**
     * The internal (unique) instance.
     */
    private static OutputManager instance;
    /**
     * The current threading mode
     */
    private static int currentMode = -1;
    
    /**
     * Gets the unique instance of the manager.
     * @return The manager object.
     */
    public static OutputManager getInstance()
    {
        if(instance == null)
            setMode(MODE_SINGLE_THREAD);
            
        return instance;
    }
    
    /**
     * Configures the output theading mode.
     * @param mode The mode identifier. Check the constants in this class.
     */
    public static void setMode(int mode)
    {
        if(mode == currentMode)
            return;
        
        switch(mode)
        {
            case MODE_SINGLE_THREAD : 
            {
                if(instance != null)
                    instance = new SingleThreadOutputManager(((MultiThreadOutputManager)instance).getCurrentMap());
                else
                    instance = new SingleThreadOutputManager();
                
                break;
            } 
            case MODE_MULTI_THREAD :
            {
                instance = new MultiThreadOutputManager(((SingleThreadOutputManager)instance).getCurrentMap());
                break;
            }
            default : 
            {
                throw new IllegalArgumentException("The provided mode is not valid.");
            }
        }
        
        currentMode = mode;
    }
    
    /**
     * Creates a new configuration builder object.
     * @return The configuration builder object.
     */
    public static OutputConfigBuilder createConfigBuilder()
    {
        return new OutputConfigBuilderImpl();
    }
    
    /**
     * Null-pointer safe method which prints text through the specified output, 
     * without a line break. It uses the instance to search for the specified 
     * output id. If no output is found with such id, the method does nothing.
     * @param id The id of the output to print.
     * @param obj The input to be printed.
     */
    public static void print(String id, Object obj)
    {
        PrintWriter ps = getInstance().getOutput(id);
        
        if(ps != null)
            ps.print(obj);
    }
    
    /**
     * Null-pointer safe method which prints text through the specified output, 
     * with line break. It uses the instance to search for the specified output 
     * id. If no output is found with such id, the method does nothing.
     * @param id The id of the output to print.
     * @param obj The input to be printed. 
     */
    public static void println(String id, Object obj)
    {
        PrintWriter ps = getInstance().getOutput(id);
        
        if(ps != null)
            ps.println(obj);
    }
    
    /**
     * Null-pointer safe method which prints an empty line through the specified 
     * output. It uses the instance to search for the specified output id. If no
     * output is found with such id, the method does nothing.
     * @param id The id of the output to print.
     */
    public static void println(String id)
    {
        PrintWriter ps = getInstance().getOutput(id);
        
        if(ps != null)
            ps.println();
    }
    
    /**
     * Null-pointer safe method which prints an formated text through the 
     * specified output. It uses the instance to search for the specified output
     * id. If no output is found with such id, the method does nothing.
     * @param id The id of the output to print.
     * @param format The format to apply for the text.
     * @param objs The objects to be used within the formatting.
     */
    public static void format(String id, String format, Object... objs)
    {
        PrintWriter ps = getInstance().getOutput(id);
        
        if(ps != null)
            ps.format(id, objs);
    }
    
    /**
     * Configures a set of outputs that are specified in a configuration object.
     * If the configuration enables the use of the system output, this method
     * should configure a default output stream/writer, such as the system
     * console.
     * @param config The configuration object.
     */
    public abstract void setOutputsFromConfig(OutputConfig config);
    
    /**
     * Configures a set of outputs that are specified in a configuration object.
     * If the configuration enables the use of the system output, this method
     * should configure the provided writer for the system output.
     * @param config The configuration object.
     * @param systemWriter A custom writer to be used as the system output.
     */
    public abstract void setOutputsFromConfig(OutputConfig config, Writer systemWriter);    
    
    /**
     * Gets a writer related to the provided identifier. Such writer should be
     * redirected to all the outputs registered under the provided id.
     * @param id The id of the output to print.
     * @return The writer as a PrintWriter object.
     */
    public abstract PrintWriter getOutput(String id);
    
    /**
     * Finishes all the active outputs in the manager.
     */
    public abstract void closeOutputs();
    
    /**
     * Finishes all the active outputs in the manager that are associated with
     * the specified identifier.
     * @param id The id of the outputs to finalize.
     */
    public abstract void closeOutputs(String id);
    
    /**
     * Clear all the outputs registered in this manager. Any output marked as
     * no disposable may be skipped.
     */
    public abstract void clearOutputs();
    
    /**
     * Clear all the outputs registered in this manager that are associated with
     * the specified identifier. Any output marked as no disposable may be 
     * skipped.
     * @param id The id of the outputs to clear.
     */
    public abstract void clearOutputs(String id);
}