package tqllang;

/**
 * Yas
 */
public class ObservationCollectionVariable extends CollectionVariable
{
    public SensorCollectionVariable sensorVariable;

    public ObservationCollectionVariable(String name)
    {
        super(name, CollectionType.observation);
    }
}
