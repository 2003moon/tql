package tqllang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yas.
 */

public class SQLQuery
{
    public String command;

    public List<CollectionVariable> fromCollections;
    public List<String> attributesList;
    public String where;

    public SQLQuery()
    {
        command = "";
        fromCollections = new ArrayList<>();
        attributesList = new ArrayList<>();
        where = "";
    }
}


