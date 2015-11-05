
package optefx.util.output3;

/**
 * The default output configuration object. Stores all the output information in
 * arrays, all of them related by their indexes. Therefore, the object is 
 * immutable.
 * @author Enrique Urra C.
 */
class OutputConfigImpl implements OutputConfig
{    
    /**
     * An array that stores the configuration ids.
     */
    private String[] ids;
    /**
     * An array that stores the enabled/disabled system output option for each
     * identifier.
     */
    private boolean[] systemOut;
    /**
     * An array that stores the disposable status for each identifier.
     */
    private boolean[] disposableStatus;
    /**
     * A matrix of file configuration objects, for which each file stores the
     * file outputs related with each id in the configuration.
     */
    private FileConfig[][] fileConfigs;

    /**
     * Constructor which accepts the array structures.
     * @param ids The array of identifiers of this configuration.
     * @param systemOut The array of enabled/disabled system output options.
     * @param fileConfigs The matrix of file configuration objects.
     */
    public OutputConfigImpl(String[] ids, boolean[] systemOut, boolean[] disposableStatus, FileConfig[][] fileConfigs)
    {
        this.ids = ids;
        this.systemOut = systemOut;
        this.disposableStatus = disposableStatus;
        this.fileConfigs = fileConfigs;
    }

    @Override
    public int getIdsCount()
    {
        return ids.length;
    }

    @Override
    public boolean idUsesSystemOut(int idIndex)
    {
        return systemOut[idIndex];
    }

    @Override
    public boolean isIdDisposable(int idIndex)
    {
        return disposableStatus[idIndex];
    }

    @Override
    public String getId(int idIndex)
    {
        return ids[idIndex];
    }

    @Override
    public int getIdFileOutputCount(int idIndex)
    {
        return fileConfigs[idIndex].length;
    }

    @Override
    public String getIdFileOutputPath(int idIndex, int outputIndex)
    {
        return fileConfigs[idIndex][outputIndex].getPath();
    }

    @Override
    public boolean getIdFileOutputAppend(int idIndex, int outputIndex)
    {
        return fileConfigs[idIndex][outputIndex].getAppend();
    }
}
