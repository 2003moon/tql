package tqllang;

/**
 * Created by Yas.
 */
public class TQLParser
{
    public TQLScanner scanner;
    public TQLQuery tqlQuery;
    public Token token;

    public TQLParser(String query)
    {
        tqlQuery = new TQLQuery();
        scanner = new TQLScanner(query);
        token = scanner.getToken();
    }

    public void eatToken()
    {
        token = scanner.getToken();
    }

    public void parse()
    {
        command();
    }

    public void command()
    {
        if(token == Token.selectToken)
        {
            tqlQuery.command = scanner.identifier;

            // eat it
            eatToken();

            // parse "attributes"
            attributes();

            // parse "from"
            from();

            // parse "where"
            where();
        }
        else
        {
            error("No command");
        }
    }

    public void attributes()
    {

    }

    public void from()
    {
        if(token == Token.fromToken)
        {

        }
        else
        {
            error("No from statement");
        }
    }

    public void where()
    {
        if(token == Token.whereToken)
        {

        }
    }


    public void error(String text)
    {
        System.out.println(text);
    }
}
