package tqllang;

/**
 * Created by Yas.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TQLScanner
{
    private TQLReader reader;
    private char inputChar;
    public String identifier;       // if it sees a string, it will be stored here
    private String identifierString;    // this is to build the characters and eventually store the final in identifierName
    private HashMap<String, Token> keywordTable;
    private LinkedList<String> identifierTable;
    private double number;  // if it sees a constant number, the value will be stored here
    private int id;
    private boolean firstRun;
    private int currentLine;
    private int currentCharPosition;
    private HashSet<Character> otherCharacters;

    public TQLScanner(String query)
    {
        reader = new TQLReader(query);
        identifierString = "";

        otherCharacters = new HashSet<Character>();
        otherCharacters.add('=');
        otherCharacters.add('*');
        otherCharacters.add(',');
        otherCharacters.add(';');
        otherCharacters.add('(');
        otherCharacters.add(')');

        // pre-populate keyword table with keywords
        keywordTable = new HashMap<>(15);
        keywordTable.put("define", Token.defineToken);
        keywordTable.put("sensorcollection", Token.sensorToken);
        keywordTable.put("observationcollection", Token.observationToken);
        keywordTable.put("sensors_to_observations", Token.sensorToObsToken);
        keywordTable.put("select", Token.selectToken);
        keywordTable.put("as", Token.asToken);
        keywordTable.put("from", Token.fromToken);
        keywordTable.put("where", Token.whereToken);

        identifierTable = new LinkedList<>();
        firstRun = true;
        currentLine = 1;
        currentCharPosition = 1;
        next();
    }

    private void next()
    {
        // don't check if it's the first run
        // don't get any character if
        if(!firstRun)
        {
            if (inputChar == 0x00 || inputChar == 0xff || inputChar == (char)-1)
                return;
        }

        try
        {
            inputChar = reader.getCharacter();
        }
        catch(IOException e)
        {
            inputChar = 0x00;
            return;
        }

        currentCharPosition += 1;

        if(inputChar == '\n')
        {
            currentLine += 1;
            currentCharPosition = 0;
        }
    }

    public Token getToken(boolean whereClause)
    {
        // don't eat spaces if you are reading for where clause
        if(!whereClause)
            eatSpaces();

        // error character, end of file characters
        if (inputChar == 0x00)
        {
            error("Error in reading");
            return Token.errorToken;
        }

        // skip the characters if it's a comment
        /*while(inputChar == '#')
        {
            skipCharacters();
            eatSpaces();
        }*/

        identifierString = "";

        switch (inputChar)
        {
            case (char)-1:
                System.out.println("Reached end of file");
                return Token.endOfFileToken;
            case '*':
                // eat "*"
                next();
                identifier = "*";
                return Token.timesToken;
            case '=':
                // eat "="
                next();
                identifier = "=";
                return Token.eqlToken;
            case ',':
                // eat ","
                next();
                identifier = ",";
                return Token.commaToken;
            case ')':
                // eat ")"
                next();
                identifier = ")";
                return Token.closeparenToken;
            case '(':
                // eat "("
                next();
                identifier = "(";
                return Token.openparenToken;
            case ';':
                next();
                identifier = ";";
                return Token.semiToken;
            default:

                if(Character.isLetter(inputChar))
                {
                    // TODO: Think
                    while (Character.isLetterOrDigit(inputChar) || inputChar == '.' || inputChar == '_' || inputChar == '*')
                    {
                        identifierString = identifierString+inputChar;
                        next();
                    }

                    identifier = identifierString;

                    if(keywordTable.containsKey(identifierString.toLowerCase()))
                    {
                        return keywordTable.get(identifierString.toLowerCase());
                    }
                    else
                    {
                        return Token.identToken;
                    }
                }

                if(whereClause)
                {
                    identifier = ""+inputChar;
                    next();
                    return Token.identToken;
                }

                return Token.errorToken;
        }
    }

    public boolean isKeyword(String name)
    {
        return keywordTable.containsKey(name);
    }

    private void eatSpaces()
    {
        while(Character.isWhitespace(inputChar))
        {
            next();
        }
    }

    private void skipCharacters()
    {
        // keep reading characters until you hit end of line
        while(inputChar != '\n')
        {
            next();
        }

        // eat end of line
        next();
    }

    private void error(String errorMsg)
    {
        inputChar = 0x00;
        System.out.println(errorMsg);
    }
}
