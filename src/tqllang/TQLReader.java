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

    public char getCharacter() throws IOException
    {
        return (char) reader.read();
    }
}
