package tqllang;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Yas
 */
public class WhereScanner
{
    private TQLReader reader;
    private char inputChar;
    public String identifier;       // if it sees a string, it will be stored here
    private String identifierString;    // this is to build the characters and eventually store the final in identifierName
    private HashMap<String, Token> keywordTable;
    //private LinkedList<String> identifierTable;
    //private double number;  // if it sees a constant number, the value will be stored here
    //private int id;
    private boolean firstRun;
    private int currentLine;
    private int currentCharPosition;
    //private HashSet<Character> otherCharacters;

    public WhereScanner(String whereClause)
    {
        reader = new TQLReader(whereClause);
        identifierString = "";

        // pre-populate keyword table with keywords
        keywordTable = new HashMap<>(15);
        keywordTable.put("and", Token.andToken);
        keywordTable.put("or", Token.orToken);
        keywordTable.put("in", Token.inToken);

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

    public Token getToken()
    {
        // error character, end of file characters
        if (inputChar == 0x00)
        {
            error("Error in reading");
            return Token.errorToken;
        }

        identifierString = "";

        if(inputChar == (char)-1)
        {
            System.out.println("Reached end of file");
            return Token.endOfFileToken;
        }
        else if(inputChar == '\"')
        {
            identifierString = identifierString+inputChar;
            next();

            // keep reading until you find "
            while(inputChar != '\"')
            {
                identifierString = identifierString+inputChar;
                next();
            }

            if(inputChar != '\"')
            {
                error("Missing \" ");
            }

            identifierString = identifierString+inputChar;
            identifier = identifierString;

            next();

            return Token.stringToken;
        }
        else if(inputChar == '\'')
        {
            identifierString = identifierString+inputChar;
            next();

            // keep reading until you find "
            while(inputChar != '\'')
            {
                identifierString = identifierString+inputChar;
                next();
            }

            if(inputChar != '\'')
            {
                error("Missing \' ");
            }

            next();
            identifierString = identifierString+inputChar;

            identifier = identifierString;
            return Token.stringToken;

        }
        else if(Character.isLetter(inputChar))
        {
            // this is an identifier
            // TODO: Think
            while (Character.isLetterOrDigit(inputChar) || inputChar == '.' || inputChar == '_')
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
        else
        {
            // just return the character as is
            identifier = inputChar+"";
            next();
            return Token.characterToken;
        }
    }

    private void error(String errorMsg)
    {
        inputChar = 0x00;
        System.out.println(errorMsg);
    }
}
