package tqllang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yas.
 */
public class TQLTranslator
{
    public String translate(TQLQuery tqlQuery)
    {
        // start with the final query
        String allQuery = translateSQL(tqlQuery.finalQuery);

        return allQuery;
    }

    public String translateSQL(SQLQuery sqlQuery)
    {
        String translatedQuery = "SELECT ";

        // parse the "where" clause first
        translateWhere(sqlQuery);

        //TODO: attributes
        for(int i = 0; i < sqlQuery.attributesList.size(); i++)
        {
            // if the last attribute, don't put a comma
            if(i == sqlQuery.attributesList.size()-1)
            {
                translatedQuery += sqlQuery.attributesList.get(i);
            }
            else
            {
                translatedQuery += sqlQuery.attributesList.get(i)+",";
            }
        }


        translatedQuery += " FROM ";

        // expanding the tables in the "from"
        for(CollectionVariable collection : sqlQuery.fromCollections)
        {
            if(collection.type == CollectionType.sensor)
            {
                translatedQuery += expandSensor((SensorCollectionVariable) collection);
            }
            else if(collection.type == CollectionType.observation)
            {
                translatedQuery += expandObservation((ObservationCollectionVariable) collection);
            }
            else
            {
                translatedQuery += expandTable(collection);
            }

            translatedQuery += " AS "+ collection.alias;
        }

        return translatedQuery;
    }

    public void translateWhere(SQLQuery query)
    {
        // create where clause
        WhereClause whereClause = new WhereClause();

        List<String> joinConditions = new ArrayList<>();
        String modifiedWhere = "";

        WhereCondition whereCondition = new WhereCondition();

        WhereScanner whereScanner = new WhereScanner(query.where);
        Token token = whereScanner.getToken();

        while(token != Token.endOfFileToken)
        {
            // if token is identifier, then figure out the join (if you actually need a join)
            if(token == Token.identToken)
            {
                whereCondition = figureOutJoins(query, whereScanner.identifier);

                joinConditions.add(whereCondition.joinCondition);
                modifiedWhere += whereCondition.originalCondition;
            }
            else
            {
                modifiedWhere += whereScanner.identifier;
            }

            token = whereScanner.getToken();
        }



    }

    public WhereCondition figureOutJoins(SQLQuery query, String attribute)
    {
        WhereCondition whereCondition = new WhereCondition();

        String[] array = attribute.split(".");

        String previous = array[0];

        // TODO: should you check if the table is not mentioned in the from clause and add it?

        String current = "";

        for(int i = 1; i < array.length; i++)
        {
            current = array[i];
        }

        return whereCondition;
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
