package tqllang;

/**
 * Created by Yas.
 */
public class TQLTranslator
{
    public String translate(TQLQuery tqlQuery)
    {
        // start with the final query
        String finalQuery = "SELECT ";

        //TODO: attributes
        for(int i = 0; i < tqlQuery.finalQuery.attributesList.size(); i++)
        {
            // if the last attribute, don't put a comma
            if(i == tqlQuery.finalQuery.attributesList.size()-1)
            {
                finalQuery += tqlQuery.finalQuery.attributesList.get(i);
            }
            else
            {
                finalQuery += tqlQuery.finalQuery.attributesList.get(i)+",";
            }
        }


        finalQuery += " FROM ";

        // expanding the tables in the "from"
        for(CollectionVariable collection : tqlQuery.finalQuery.fromCollections)
        {
            if(collection.type == CollectionType.sensor)
            {
                finalQuery += expandSensor((SensorCollectionVariable) collection);
            }
            else if(collection.type == CollectionType.observation)
            {
                finalQuery += expandObservation((ObservationCollectionVariable) collection);
            }
            else
            {
                finalQuery += expandTable(collection);
            }

            finalQuery += " AS "+ collection.alias;
        }

        return finalQuery;
    }

    public String expandSensor(SensorCollectionVariable sensorVariable)
    {
        String sensorQuery = "SELECT ";

        // TODO: put in the attributes list
        for(int i = 0; i < sensorVariable.query.attributesList.size(); i++)
        {
            // if the last attribute, don't put a comma
            if(i == sensorVariable.query.attributesList.size()-1)
            {
                sensorQuery += sensorVariable.query.attributesList.get(i);
            }
            else
            {
                sensorQuery += sensorVariable.query.attributesList.get(i)+",";
            }
        }

        sensorQuery += " FROM ";

        for(CollectionVariable collection : sensorVariable.query.fromCollections)
        {
            if(collection.type == CollectionType.sensor)
            {
                sensorQuery += expandSensor((SensorCollectionVariable) collection);
            }
            else if(collection.type == CollectionType.observation)
            {
                sensorQuery += expandObservation((ObservationCollectionVariable) collection);
            }
            else
            {
                sensorQuery += expandTable(collection);
            }
        }


        return sensorQuery;
    }

    public String expandObservation(ObservationCollectionVariable observationVariable)
    {
        // get the SQL of the sensor
        String sensorQuery = expandSensor(observationVariable.sensorVariable);

        String observationQuery = "( SELECT "+observationVariable.name+".* ";

        observationQuery += "FROM Observation AS "+observationVariable.name+ " JOIN ( ";
        observationQuery += sensorQuery + " ) AS "+observationVariable.sensorVariable.name;
        observationQuery += " ON ("+observationVariable.name+".sen_id = "+observationVariable.sensorVariable.name+".id) )";

        return observationQuery;
    }

    public String expandTable(CollectionVariable variable)
    {
        String table = variable.name+" AS "+variable.alias;

        return table;
    }
}
