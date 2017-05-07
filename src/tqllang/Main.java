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
            Scanner input = new Scanner(new FileInputStream("example"));
            while(input.hasNextLine())
            {
                query += input.nextLine()+"\n";
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        TQLParser p = new TQLParser(query);

        try
        {
            p.parse();
        }
        catch(TQLException e)
        {
            System.out.println(e.getMessage());
        }

    }
}