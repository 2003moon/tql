package tqllang;

import java.util.ArrayList;

/**
 * Created Yas
 */

public class JoinTables
{
    public ArrayList<JoinTable> joinTables;

    public JoinTables()
    {
        joinTables = new ArrayList<>();
    }

    public void addJoinTable(String table, String alias, String column)
    {
        // check if the table already exists
        for(JoinTable joinTable : joinTables)
        {
            if(joinTable.table.equals(table))
                return;
        }

        // TODO: don't use this. This was for translator 1
        JoinTable jTable = new JoinTable(table, alias, column, column);
        joinTables.add(jTable);
    }

}
