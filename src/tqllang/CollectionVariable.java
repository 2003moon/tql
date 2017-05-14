package tqllang;

/**
 * Yas
 */

public class CollectionVariable
{
    public String name;
    public String alias;
    public boolean isAssigned;
    public CollectionType type;

    public CollectionVariable(String name, CollectionType type)
    {
        this.name = name;
        this.type = type;
    }

    public CollectionVariable(String name, String alias, CollectionType type)
    {
        this.name = name;
        this.alias = alias;
        this.type = type;
    }

}
