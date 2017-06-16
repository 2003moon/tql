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
            Scanner input = new Scanner(new FileInputStream("example5"));

            while(input.hasNextLine())
            {
                query += input.nextLine()+"\n";
            }

            TQLParser tqlParser = new TQLParser(query);
         //   TQLTranslator2 translator = new TQLTranslator2();
            TQLTranslator3 translator = new TQLTranslator3();

            try
            {
                tqlParser.parse();
                System.out.println(translator.translate(tqlParser.tqlQuery));
                DoQuery dq= new DoQuery();
                dq.Query(translator.translate(tqlParser.tqlQuery));
                dq.getResult();
            }
            catch(TQLException e)
            {
                System.out.println(e.getMessage());
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
}