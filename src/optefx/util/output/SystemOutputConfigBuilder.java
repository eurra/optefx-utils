
package optefx.util.output;

import java.io.OutputStream;
import java.io.Writer;

/**
 *
 * @author Enrique Urra C.
 */
public interface SystemOutputConfigBuilder extends OutputConfigBuilder
{
    SystemOutputConfigBuilder setSystemOutput(Writer output);
    SystemOutputConfigBuilder setSystemOutput(OutputStream output);
    SystemOutputConfigBuilder setSystemErrorOutput(Writer output);
    SystemOutputConfigBuilder setSystemErrorOutput(OutputStream output);
}
