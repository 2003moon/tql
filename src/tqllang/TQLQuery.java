package tqllang;

import java.util.HashMap;

/**
 * Yas
 */
public class TQLQuery
{
    public HashMap<String,SensorCollectionVariable> sensorVariables;
    public HashMap<String,ObservationCollectionVariable> observationVariables;
    public SQLQuery finalQuery;

    public TQLQuery()
    {
        sensorVariables = new HashMap<>();
        observationVariables = new HashMap<>();
        finalQuery = new SQLQuery();
    }
}
