package tqllang;

/**
 * Created by Yas
 */
public class Relationship
{
    public String tableName;
    public String column;
    public RelationshipType type;

    public Relationship(String tn, String c, RelationshipType t)
    {
        tableName = tn;
        column = c;
        type = t;
    }

    public Relationship()
    {

    }
}
