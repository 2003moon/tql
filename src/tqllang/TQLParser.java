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

        System.out.println();
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
                SQLQuery query = new SQLQuery();

                // assign this query to the sensor variable
                tqlQuery.sensorVariables.get(variableName).query = query;

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
                        // do some logic
                        if(tqlQuery.observationVariables.containsKey(variableName) && tqlQuery.sensorVariables.containsKey(scanner.identifier))
                        {
                            tqlQuery.observationVariables.get(variableName).sensorVariable = tqlQuery.sensorVariables.get(scanner.identifier);
                        }
                        else
                        {
                            // error
                            if(!tqlQuery.sensorVariables.containsKey(scanner.identifier))
                            {
                                throw new TQLException("Sensor variable is not defined.");
                            }
                            else
                            {
                                throw new TQLException("Observation variable is not defined.");
                            }
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
                            throw new TQLException("Missing \")\" for sensor_to_observations.");
                        }
                    }
                    else
                    {
                        // error
                        throw new TQLException("Error with sensor variable.");
                    }
                }
                else
                {
                    // error
                    throw new TQLException("Syntax error for sensor_to_observations.");
                }
            }
            else
            {
                // error
                throw new TQLException("Unexpected expression for variable assignment.");
            }
        }
        else
        {
            // error
            throw new TQLException("Missing \"=\" after variable.");
        }

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
            if(!tqlQuery.sensorVariables.containsKey(variableName))
            {
                tqlQuery.sensorVariables.put(variableName, new SensorCollectionVariable(variableName));
            }
            else
                throw new TQLException("Sensor variable is already defined");
        }
        else
        {
            if(!tqlQuery.observationVariables.containsKey(variableName))
            {
                tqlQuery.observationVariables.put(variableName, new ObservationCollectionVariable(variableName));
            }
            else
                throw new TQLException("Observation variable is already defined");
        }

    }

    public void addTableToQuery(SQLQuery query)
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

        // TODO: give warning if a variable is not assigned
        if(tqlQuery.sensorVariables.containsKey(collectionName))
        {
            tqlQuery.sensorVariables.get(collectionName).alias = aliasName;
            query.fromCollections.add(tqlQuery.sensorVariables.get(collectionName));
        }
        else if(tqlQuery.observationVariables.containsKey(collectionName))
        {
            tqlQuery.observationVariables.get(collectionName).alias = aliasName;
            query.fromCollections.add(tqlQuery.observationVariables.get(collectionName));
        }
        else
        {
            query.fromCollections.add(new CollectionVariable(collectionName, aliasName, CollectionType.table));
        }
    }
}
