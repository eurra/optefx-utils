
package optefx.util.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Enrique Urra C.
 */
public final class WrapperWriterBuilder implements WriterBuilder
{
    private final Writer wrapped;

    public WrapperWriterBuilder(Writer wrapped)
    {
        if(wrapped == null)
            throw new NullPointerException("Null wrapped writer");
        
        this.wrapped = wrapped;
    }

    @Override
    public Writer build() throws IOException
    {
        return wrapped;
    }
}
