package tqllang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yas.
 */

public class TQLQuery
{
    public String command;

    public List<CollectionVariable> fromCollections;
    public List<String> attributesList;
    public String where;

    public TQLQuery()
    {
        command = "";
        fromCollections = new ArrayList<>();
        attributesList = new ArrayList<>();
        where = "";
    }
}


