package tqllang;

import java.util.LinkedHashMap;

/**
 * Yas
 */
public class TQLQuery
{
    //public HashMap<String,SensorCollectionVariable> sensorVariables;
    //public HashMap<String,ObservationCollectionVariable> observationVariables;
    public LinkedHashMap<String, CollectionVariable> collectionVariables;
    public SQLQuery finalQuery;

    public TQLQuery()
    {
        //sensorVariables = new HashMap<>();
        //observationVariables = new HashMap<>();
        collectionVariables = new LinkedHashMap<>();
        finalQuery = new SQLQuery();
    }
}
