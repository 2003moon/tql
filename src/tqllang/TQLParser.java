package tqllang;

import java.util.HashMap;

/**
 * Created by Yas.
 */
public class TQLParser
{
    public TQLScanner scanner;
    public Token token;
    public TQLQuery tqlQuery;

    public TQLParser(String query)
    {
        scanner = new TQLScanner(query);
        token = scanner.getToken(false);
        tqlQuery = new TQLQuery();
    }

    public void eatToken(boolean whereClause)
    {
        token = scanner.getToken(whereClause);
    }

    public void parse() throws TQLException
    {
        // keep parsing until you reach the final query indicated by encountering "select"
        while(token != Token.selectToken)
        {
            if(token == Token.defineToken)
            {
                // parse define statement
                define();
            }
            else if(token == Token.identToken)
            {
                // parse assignment statement
                assign();
            }
            else
            {
                // error
                throw new TQLException("Unexpected statement!");
            }

            if(token != Token.semiToken)
            {
                // error. statement not ending with ";"
                throw new TQLException("Missing \";\" at end of statement.");
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
            selectStatement(tqlQuery.finalQuery);
        }
        else
        {
            // error
            throw new TQLException("Missing final query.");
        }

        System.out.println("Done parsing!");
    }

    public void define() throws TQLException
    {
        // eat "DEFINE"
        eatToken(false);

        // identify the type of the variable
        if(token == Token.sensorToken || token == Token.observationToken)
        {
            Token varType = token;

            // eat SensorCollection/ObservationCollection keyword
            eatToken(false);

            if(token == Token.identToken)
            {
                // try to add the variable our defined variables list
                addCollectionVariable(scanner.identifier, varType);

                // eat the identifier
                eatToken(false);

                while(token == Token.commaToken)
                {
                    // eat the ","
                    eatToken(false);

                    // try to add the variable our defined variables list
                    addCollectionVariable(scanner.identifier, varType);

                    // eat the identifier
                    eatToken(false);
                }
            }
            else
            {
                // error
                throw new TQLException("Variable name is missing.");
            }
        }
        else
        {
            // error
            throw new TQLException("The type of variable is missing.");
        }
    }

    public void assign() throws TQLException
    {
        // store the variable name of the collection
        String variableName = scanner.identifier;

        if(!tqlQuery.collectionVariables.containsKey(variableName))
            throw new TQLException("Variable "+variableName+" is not defined");

        // eat the identifier
        eatToken(false);

        if(token == Token.eqlToken)
        {
            // eat "="
            eatToken(false);

            if(token == Token.selectToken)
            {
                SQLQuery query = new SQLQuery();

                // assign this query to the sensor variable
                ((SensorCollectionVariable)tqlQuery.collectionVariables.get(variableName)).query = query;

                // parse the select statement and put it in the query of the sensor variable
                selectStatement(query);

            }
            else if(token == Token.sensorToObsToken)
            {
                // eat the keyword
                eatToken(false);

                if(token == Token.openparenToken)
                {
                    // eat "("
                    eatToken(false);

                    if(token == Token.identToken)
                    {
                        // the scanner.identifier is the sensor variable to be assigned to the observation
                        if(tqlQuery.collectionVariables.containsKey(scanner.identifier))
                        {
                            ((ObservationCollectionVariable)tqlQuery.collectionVariables.get(variableName)).sensorVariable = (SensorCollectionVariable) tqlQuery.collectionVariables.get(scanner.identifier);
                        }
                        else
                        {
                            throw new TQLException("Sensor variable "+scanner.identifier+" is not defined.");
                        }

                        // eat the ident.
                        eatToken(false);

                        if(token == Token.closeparenToken)
                        {
                            // eat the ")"
                            eatToken(false);
                        }
                        else
                        {
                            throw new TQLException("Missing \")\" for sensor_to_observations at "+variableName+".");
                        }
                    }
                    else
                    {
                        // error
                        throw new TQLException("Error with sensor variable "+scanner.identifier+" assigned to observation.");
                    }
                }
                else
                {
                    // error
                    throw new TQLException("Syntax error for sensor_to_observations at variable "+variableName+".");
                }
            }
            else
            {
                // error
                throw new TQLException("Unexpected expression for variable assignment for "+variableName+".");
            }
        }
        else
        {
            // error
            throw new TQLException("Missing \"=\" after variable "+variableName+".");
        }

        // by now, assignment should have been successful
        tqlQuery.collectionVariables.get(variableName).isAssigned = true;
    }

    public void selectStatement(SQLQuery query) throws TQLException
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
            throw new TQLException("Missing \"from\" statement.");
        }

        // where
        if(token == Token.whereToken)
        {
            // eat "where"
            eatToken(true);

            where(query);
        }
    }

    public void select(SQLQuery query) throws TQLException
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
                // TODO: check for duplicate attributes?
                query.attributesList.add(scanner.identifier);

                // eat the ident.
                eatToken(false);

                while(token == Token.commaToken)
                {
                    // TODO: check for duplicate attributes?
                    query.attributesList.add(scanner.identifier);

                    // eat the ident.
                    eatToken(false);
                }
            }
            else
            {
                // error
                throw new TQLException("Unexpected attributes in \"select\".");
            }
        }

    }

    public void from(SQLQuery query) throws TQLException
    {
        if(token == Token.identToken)
        {
            addTableToQuery(query);

            while(token == Token.commaToken)
            {
                // eat ","
                eatToken(false);

                if(token == Token.identToken)
                {
                    // do some logic
                    addTableToQuery(query);
                }
                else
                {
                    // error
                    throw new TQLException("Unexpected identifier in \"from\".");
                }
            }
        }
        else
        {
            // error
            throw new TQLException("Unexpected identifier in \"from\".");
        }
    }

    public void where(SQLQuery query)
    {
        // TODO: fix this for keywords after where clause
        while(token != Token.semiToken && !scanner.isKeyword(scanner.identifier))
        {
            query.where += scanner.identifier;
            eatToken(true);
        }
    }

    public void addCollectionVariable(String variableName, Token variableType) throws TQLException
    {
        if(variableType == Token.sensorToken)
        {
            if(!tqlQuery.collectionVariables.containsKey(variableName))
            {
                tqlQuery.collectionVariables.put(variableName, new SensorCollectionVariable(variableName));
            }
            else
                throw new TQLException("Sensor variable "+variableName+" is already defined");
        }
        else
        {
            if(!tqlQuery.collectionVariables.containsKey(variableName))
            {
                tqlQuery.collectionVariables.put(variableName, new ObservationCollectionVariable(variableName));
            }
            else
                throw new TQLException("Observation variable "+variableName+" is already defined");
        }

    }

    public void addTableToQuery(SQLQuery query) throws TQLException
    {
        // do some logic
        String collectionName = scanner.identifier;
        String aliasName = "";

        // eat the ident.
        eatToken(false);

        // TODO: check if it has an alias. We should enforce it
        if(token == Token.identToken)
        {
            aliasName = scanner.identifier;
            eatToken(false);
        }
        else
            throw new TQLException("Missing alias name for collection "+collectionName);

        // TODO: give warning if a variable is not assigned
        // collection in the "from" clause should be either collection variable or system-defined table
        if(tqlQuery.collectionVariables.containsKey(collectionName))
        {
            Collection collection;
            CollectionVariable collectionVariable = tqlQuery.collectionVariables.get(collectionName);

            collection = collectionVariable.createCollection();
            collection.alias = aliasName;
            query.fromCollections.add(collection);
        }
        else
        {
            // get the type of the collection
            CollectionType collectionType = CollectionTypeMapping.getTypeOf(collectionName);

            // TODO: check if collection type used is not one of the system-defined
            if(collectionType == CollectionType.noType)
                throw new TQLException("Could not recognize collection "+collectionName);

            query.fromCollections.add(new Collection(collectionName, aliasName, collectionType));
        }
    }
}
