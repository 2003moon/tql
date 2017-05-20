package tqllang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yas.
 */

public class SQLQuery
{
    public String command;

    public List<Collection> fromCollections;
    public List<String> attributesList;         // TODO: better to model as an object
    public String where;                        // TODO: better to model as an object

    public SQLQuery()
    {
        command = "";
        fromCollections = new ArrayList<>();
        attributesList = new ArrayList<>();
        where = "";
    }
}


