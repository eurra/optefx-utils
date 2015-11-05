
package optefx.util.output;

/**
 *
 * @author Enrique Urra C.
 */
public interface OutputConfigBuilder
{
    OutputConfigBuilder addOutput(String id, WriterBuilder builder);
    OutputConfigBuilder makePermanent(String id);
    OutputConfig build();
}
