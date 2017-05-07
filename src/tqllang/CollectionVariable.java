package tqllang;

/**
 * Yas
 */

public class CollectionVariable
{
    public String name;
    public String alias;
    public boolean isAssigned;

    public CollectionVariable(String name)
    {
        this.name = name;
    }

    public CollectionVariable(String name, String alias)
    {
        this.name = name;
        this.alias = alias;
    }

}
