package tqllang;

import java.util.ArrayList;

/**
 * Created by Yas
 */
public class Relationship
{
    public String fieldType;
    public RelationshipType type;
    public ArrayList<JoinTable> joinInformation;

    public Relationship()
    {
        this.joinInformation = new ArrayList<>();
    }
}
