package tqllang;

/**
 * Created by Yas.
 */
import java.io.*;

public class TQLReader
{
    private Reader reader;

    public TQLReader(String query)
    {
        reader = new StringReader(query);
    }
    public char getCharacter()
    {
        try
        {
            return (char) reader.read();
        }
        catch(IOException e)
        {
            // TODO: should you close the reader? if yes, TQLScanner might need to handle some exception
            error(e);
            return 0x00;
        }
    }

    public void error(Exception e)
    {
        if(e instanceof IOException)
        {
            System.out.println("Error encountered while reading.");
            System.out.println(e.getMessage());
//            System.exit(1);
        }
    }
}
