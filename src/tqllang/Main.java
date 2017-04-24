package tqllang;


public class Main
{
    public static void main(String[] args)
    {
        TQLParser p = new TQLParser("SELECT name, id FROM Sensor");
        p.parse();
    }
}