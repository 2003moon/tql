package tqllang;

/**
 * Created by Yas.
 */
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

        inputChar = reader.getCharacter();
        currentCharPosition += 1;

        if(inputChar == '\n')
        {
            currentLine += 1;
            currentCharPosition = 0;
        }
    }

    public Token getToken(boolean whereClause)
    {
        eatSpaces();

        // error character, end of file characters
        if (inputChar == 0x00)
        {
            error("Error in reading");
            return Token.errorToken;
        }

        // skip the characters if it's a comment
        while(inputChar == '#')
        {
            skipCharacters();
            eatSpaces();
        }

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
            /*case '/':
                // eat "/"
                next();
                if(inputChar != '/')
                {
                    identifier = "/";
                    return Token.divToken;
                }


                // it's a comment and inputChar == '/', the 2nd one
                skipCharacters();
                eatSpaces();

                // inputChar is now at the beginning of the next new line
                // call get token to see what token it is
                return getToken();

            case '+':
                // eat "+"
                next();
                identifier = "+";
                return Token.plusToken;
            case '-':       // TODO: negative numbers
                // eat "-"
                next();
                identifier = "-";
                return Token. minusToken;*/
            case '=':
                // eat "="
                next();
                identifier = "=";
                return Token.eqlToken;
                /*
                if(inputChar == '=')
                {
                    next();
                    return Token.eqlToken;
                }
                else
                {
                    error("Error with \"=\"");
                    return Token.errorToken;
                }*/
            /*
            case '!':
                next();
                if (inputChar == '=')
                {
                    next();
                    return Token.neqToken;
                }
                else
                {
                    error("Error with \"!\"");
                    return Token.errorToken;
                }
            case '<':
                // eat "<"
                next();
                identifier = "<";
                if (inputChar == '=')
                {
                    // eat "="
                    next();
                    return Token.leqToken;
                }
                else
                {
                    return Token.lssToken;
                }
            case '>':
                // eat ">"
                identifier = ">";
                next();
                if (inputChar == '=')
                {
                    // eat "="
                    next();
                    return Token.geqToken;
                }
                else
                {
                    return Token.gtrToken;
                }

            case '.':
                next();
                return Token.periodToken;*/
            case ',':
                // eat ","
                next();
                identifier = ",";
                return Token.commaToken;
            /*case '[':
                next();
                return Token.openbracketToken;
            case ']':
                next();
                return Token.closebracketToken;*/
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
            /*case '}':
                next();
                return Token.endToken;
            case '{':
                next();
                return Token.beginToken;*/
            default:

                if(Character.isLetter(inputChar))
                {
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

                // number token, how about negative numbers
                /*if (Character.isDigit(inputChar))
                {
                    while(Character.isDigit(inputChar))
                    {
                        identifierString = identifierString+inputChar;
                        next();
                    }

                    // TODO: characters allowed after a number is space and ']'
                    if(inputChar >= (char)0)
                    {
                        number = Integer.parseInt(identifierString);
                        return Token.numberToken;
                    }
                    else
                    {
                        error("Error after reading a number");
                        return Token.errorToken;
                    }
                }*/
                // identifier token
                /*if(Character.isLetter(inputChar))
                {
                    while (Character.isLetterOrDigit(inputChar) || inputChar == '.')
                    {
                        identifierString = identifierString+inputChar;
                        next();
                    }

                    // what are the characters allowed after a variable
                    if(inputChar >= (char)0)
                    {
                        // TODO: handle keyword
                        if(keywordTable.containsKey(identifierString.toLowerCase()))
                        {
                            identifier = identifierString;
                            return keywordTable.get(identifierString.toLowerCase());
                        }
                        else
                        {
                            // TODO:
                            id = identifierTable.indexOf(identifierString);
                            if( id == -1)
                            {
                                identifierTable.add(identifierString);
                                id = identifierTable.size()-1;
                            }

                            identifier = identifierString;
                            return Token.identToken;
                        }
                    }
                    else
                    {
                        error("Error after reading a variable");
                        return Token.errorToken;
                    }
                }

                // if no token is recognized at all
                return Token.errorToken;*/
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
        // TODO: handle exception
        inputChar = 0x00;
        System.out.println(errorMsg);
    }

    private void error(Exception e)
    {
        inputChar = 0x00;
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}
