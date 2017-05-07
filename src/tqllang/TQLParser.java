package tqllang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yas.
 */
public class TQLParser
{
    public TQLScanner scanner;
    public Token token;

    public HashMap<String,SensorCollectionVariable> sensorsTable;
    public HashMap<String,ObservationCollectionVariable> observationsTable;
    public TQLQuery finalQuery;

    public TQLParser(String query)
    {
        scanner = new TQLScanner(query);
        token = scanner.getToken(false);

        sensorsTable = new HashMap<>();
        observationsTable = new HashMap<>();
        finalQuery = new TQLQuery();
    }

    public void eatToken(boolean whereClause)
    {
        token = scanner.getToken(whereClause);
    }

    public void parse()
    {
        while(token != Token.selectToken)
        {
            if(token == Token.defineToken)
            {
                // parse define statement
                define();
            }
            else
            {
                // parse assignment statement
                assign();
            }

            if(token != Token.semiToken)
            {
                // error. statement not ending with ";"
            }
            else
            {
                // eat ";"
                eatToken(false);
            }
        }

        // parse the final query
        if(token == Token.selectToken)
        {
            selectStatement(finalQuery);
        }
        else
        {
            // error
        }

        System.out.println();
    }

    public void define()
    {
        // eat "DEFINE"
        eatToken(false);

        // identify the type of the variable
        if(token == Token.sensorToken || token == Token.observationToken)
        {
            Token varType = token;

            // eat SensorCollection/ObservationCollection
            eatToken(false);

            if(token == Token.identToken)
            {
                // do some logic for the variable
                if(varType == Token.sensorToken)
                {
                    if(!addSensor(scanner.identifier))
                    {
                        // error
                    }
                }
                else
                {
                    if(!addObservation(scanner.identifier))
                    {
                        // error
                    }
                }


                // eat the identifier
                eatToken(false);

                while(token == Token.commaToken)
                {
                    // eat the ","
                    eatToken(false);

                    // do some logic for the variable
                    if(varType == Token.sensorToken)
                    {
                        if(!addSensor(scanner.identifier))
                        {
                            // error
                        }
                    }
                    else
                    {
                        if(!addObservation(scanner.identifier))
                        {
                            // error
                        }
                    }

                    // eat the identifier
                    eatToken(false);
                }
            }
            else
            {
                // error
            }


        }
        else
        {
            // error
        }
    }

    public void assign()
    {
        // do some logic with the identifier
        String variableName = scanner.identifier;

        // eat the identifier
        eatToken(false);

        if(token == Token.eqlToken)
        {
            // eat "="
            eatToken(false);

            if(token == Token.selectToken)
            {
                TQLQuery query = new TQLQuery();

                selectStatement(query);
            }
            else if(token == Token.sensorToObsToken)
            {
                // eat the keyword
                eatToken(false);

                if(token == Token.openparenToken)
                {
                    if(token == Token.identToken)
                    {
                        // do some logic
                        if(observationsTable.containsKey(variableName) && sensorsTable.containsKey(scanner.identifier))
                        {
                            observationsTable.get(variableName).sensorCollection = sensorsTable.get(scanner.identifier);
                        }
                        else
                        {
                            // error
                        }

                        // eat the ident.
                        eatToken(false);

                        if(token == Token.closeparenToken)
                        {

                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        // error
                    }
                }
                else
                {
                    // error
                }
            }
            else
            {
                // error
                error("Expecting a select statement!");
            }
        }
        else
        {
            // error
        }

    }

    public void selectStatement(TQLQuery query)
    {
        // eat "select"
        eatToken(false);

        // select
        select(query);

        // from
        if(token == Token.fromToken)
        {
            // eat "from"
            eatToken(false);

            from(query);
        }
        else
        {
            // error
        }

        // where
        if(token == Token.whereToken)
        {
            // eat "where"
            eatToken(true);

            where(query);
        }
    }

    public void select(TQLQuery query)
    {
        // expecting attributes
        if(token == Token.timesToken)
        {
            query.attributesList.add("*");

            // eat "*"
            eatToken(false);
        }
        else
        {
            if(token == Token.identToken)
            {
                // do some logic
                query.attributesList.add(scanner.identifier);

                // eat the ident.
                eatToken(false);

                while(token == Token.commaToken)
                {
                    // do some logic
                    query.attributesList.add(scanner.identifier);

                    // eat the ident.
                    eatToken(false);
                }
            }
            else
            {
                // error
            }
        }

    }

    public void from(TQLQuery query)
    {
        if(token == Token.identToken)
        {
            addTableToQuery(query);

            while(token == Token.commaToken)
            {
                // do some logic
                addTableToQuery(query);
            }
        }
        else
        {
            // error
        }
    }

    public void where(TQLQuery query)
    {
        // TODO: fix this for keywords after where clause
        while(token != Token.semiToken && !scanner.isKeyword(scanner.identifier))
        {
            query.where += scanner.identifier;
            eatToken(true);
        }
    }

    public boolean addSensor(String sensorName)
    {
        if(!sensorsTable.containsKey(sensorName))
        {
            sensorsTable.put(sensorName, new SensorCollectionVariable(sensorName));
            return true;
        }

        return false;
    }

    public boolean addObservation(String observationName)
    {
        if(!observationsTable.containsKey(observationName))
        {
            observationsTable.put(observationName, new ObservationCollectionVariable(observationName));
            return true;
        }

        return false;
    }

    public void addTableToQuery(TQLQuery query)
    {
        // do some logic
        String collectionName = scanner.identifier;
        String aliasName = "";

        // eat the ident.
        eatToken(false);

        // check if it has an alias
        if(token == Token.identToken)
        {
            aliasName = scanner.identifier;
            eatToken(false);
        }

        if(sensorsTable.containsKey(collectionName))
        {
            sensorsTable.get(collectionName).alias = aliasName;
            query.fromCollections.add(sensorsTable.get(collectionName));
        }
        else if(observationsTable.containsKey(collectionName))
        {
            observationsTable.get(collectionName).alias = aliasName;
            query.fromCollections.add(observationsTable.get(collectionName));
        }
        else
        {
            query.fromCollections.add(new CollectionVariable(collectionName, aliasName));
        }
    }

    public void processIdentifiers()
    {

    }

    public void attributes()
    {

    }

    public void error(String text)
    {
        System.out.println(text);
    }
}
