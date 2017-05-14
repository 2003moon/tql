package tqllang;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        String query = "";

        try
        {
            Scanner input = new Scanner(new FileInputStream("example2"));
            while(input.hasNextLine())
            {
                query += input.nextLine()+"\n";
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        TQLParser tqlParser = new TQLParser(query);
        TQLTranslator translator = new TQLTranslator();

        try
        {
            tqlParser.parse();
            System.out.println(translator.translate(tqlParser.tqlQuery));
        }
        catch(TQLException e)
        {
            System.out.println(e.getMessage());
        }

    }
}