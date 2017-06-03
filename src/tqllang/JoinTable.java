package tqllang;

/**
 * Created Yas
 */

public class JoinTable
{
    public String table;
    public String alias;
    public String primaryKey;
    public String foreignKey;

    public JoinTable(String table, String alias, String primaryKey, String foreignKey)
    {
        this.table = table;
        this.alias = alias;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
    }
}
