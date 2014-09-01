
package optefx.util.metadata;

/**
 *
 * @author Enrique Urra C.
 */
public interface MetadataProvider<M extends Metadata>
{
    <T extends M> T getData(Class<T> dataType);
    <T extends M> T[] getAllData(Class<T> dataType);
    boolean hasData(Class<? extends M> dataType);
}
