package tqllang;

import java.util.HashSet;

/**
 * Created by Yas
 */
public class WhereClause
{
    public String whereCondition;
    public HashSet<String> tablesToAddToFrom;

    public WhereClause()
    {
        whereCondition = "";
        tablesToAddToFrom = new HashSet<>();

    }

}
