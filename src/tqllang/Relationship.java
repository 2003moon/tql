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

    public static Relationship getRelationship(String s1, String s2) throws TQLException
    {
        Relationship relationship = new Relationship();
        relationship.fieldType = s2;

        // TODO: ifs
        if(s1.equals("Sensor") || s1.equals("SensorCollection"))
        {
            if(s2.equals("location"))
            {
                relationship.fieldType = "Location";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Location","","id", "loc_id"));
            }
            else if(s2.equals("type"))
            {
                relationship.fieldType = "SensorType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("SensorType","","id", "sen_type_id"));
            }
            else if(s2.equals("platform"))
            {
                relationship.fieldType = "Platform";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Platform","","id", "pltfm_id"));
            }
            else if(s2.equals("user"))
            {
                relationship.fieldType = "User";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Users","","id","user_id"));
            }
            else if(s2.equals("coverageRooms"))
            {
                relationship.fieldType = "Infrastructure";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Sen_Infr","","sen_id", "id"));
                relationship.joinInformation.add(new JoinTable("Infrastructure","","id", "infr_id"));
            }
            else if(!s2.equals("id") && !s2.equals("description") && !s2.equals("name") && !s2.equals("mac") && !s2.equals("IP") && !s2.equals("port"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "sen_id";
                if(s2.equals("IP"))
                    relationship.fieldType = "ip";

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("Observation") || s1.equals("ObservationCollection"))
        {
            if(s2.equals("sensor"))
            {
                relationship.fieldType = "Sensor";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Sensor","","id", "sen_id"));
            }
            else if(s2.equals("type"))
            {
                relationship.fieldType = "ObservationType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("ObservationType","","id", "obs_type_id"));
            }
            else if(s2.equals("payload"))
            {
                relationship.type = RelationshipType.json;
            }
            else if(!s2.equals("id") && !s2.equals("timestamp"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "obs_id";
                if(s2.equals("timestamp"))
                    relationship.fieldType = "timestamps";  // TODO: check this

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("Group"))
        {
            if(!s2.equals("id") && !s2.equals("description") && !s2.equals("name"))
            {
                throwUnrecognizedException(s2);
            }

//            if(s2.equals("id"))
//                relationship.fieldType = "group_id";

            relationship.type = RelationshipType.attribute;
        }
        else if(s1.equals("User"))
        {
            if(s2.equals("groups"))
            {
                relationship.fieldType = "Group";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Grp_Usr","","user_id", "id"));
                relationship.joinInformation.add(new JoinTable("Groups","","id","group_id"));
            }
            else if(!s2.equals("email") && !s2.equals("name"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("Location"))
        {
            if(!s2.equals("x") && !s2.equals("y") && !s2.equals("z"))
            {
                throwUnrecognizedException(s2);
            }

            relationship.type = RelationshipType.attribute;
        }
        else if(s1.equals("Region"))
        {
            if(s2.equals("geometry"))
            {
                relationship.fieldType = "Location";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Reg_Loc","","reg_id", "id"));
                relationship.joinInformation.add(new JoinTable("Location","","loc_id", "id"));
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("floor"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "reg_id";
                if(s2.equals("floor"))
                    relationship.fieldType = "floors";  // TODO: check this

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("InfrastructureType"))
        {
            if(!s2.equals("id") && !s2.equals("description") && !s2.equals("name"))
            {
                throwUnrecognizedException(s2);
            }

//            if(s2.equals("id"))
//                relationship.fieldType = "infr_type_id";

            relationship.type = RelationshipType.attribute;
        }
        else if(s1.equals("Infrastructure"))
        {
            if(s2.equals("type"))
            {
                relationship.fieldType = "InfrastructureType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("InfrastructureType","","id", "infr_type_id"));
            }
            else if(s2.equals("region"))
            {
                relationship.fieldType = "Region";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Region","","id", "reg_id"));
            }
            else if(!s2.equals("id") && !s2.equals("name"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "infr_id";

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("PlatformType"))
        {
            if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description"))
            {
                throwUnrecognizedException(s2);
            }

//            if(s2.equals("id"))
//                relationship.fieldType = "pltfm_type_id";

            relationship.type = RelationshipType.attribute;
        }
        else if(s1.equals("Platform"))
        {
            if(s2.equals("type"))
            {
                relationship.fieldType = "PlatformType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("PlatformType","","id","pltfm_type_id"));
            }
            else if(s2.equals("location"))
            {
                relationship.fieldType = "Location";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Location","","id","loc_id"));
            }
            else if(s2.equals("owner"))
            {
                relationship.fieldType = "User";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("Users","","id", "user_id"));
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "pltfm_id";

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("SensorType"))
        {
            if(s2.equals("payloadSchema"))
            {
                relationship.type = RelationshipType.json;
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description") && !s2.equals("mobility"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "sen_type_id";

                relationship.type = RelationshipType.attribute;
            }

        }
        else if(s1.equals("ObservationType"))
        {
            if(s2.equalsIgnoreCase("payloadSchema"))
            {
                relationship.type = RelationshipType.json;
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "obs_type_id";

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("SemanticObservationType"))
        {
            if(s2.equals("payloadSchema"))
            {
                relationship.type = RelationshipType.json;
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "so_type_id";

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("VirtualSensorType"))
        {
            if(s2.equals("observationType"))
            {
                relationship.fieldType = "ObservationType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("ObservationType","","id","obs_type_id"));
            }
            else if(s2.equals("semanticObservationType"))
            {
                relationship.fieldType = "SemanticObservationType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("SemanticObservationType","","id","so_type_id"));
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "vs_type_id";

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("VirtualSensor"))
        {
            if(s2.equals("type"))
            {
                relationship.fieldType = "VirtualSensorType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("VirtualSensorType","","id","vs_type_id"));
            }
            else if(!s2.equals("id") && !s2.equals("name") && !s2.equals("description") && !s2.equals("sourceFileLocation") && !s2.equals("compiledCodeLocation") && !s2.equals("language") && !s2.equals("projectName"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "vs_id";
                if(s2.equals("language"))
                    relationship.fieldType = "languages";   // TODO: check this

                relationship.type = RelationshipType.attribute;
            }
        }
        else if(s1.equals("SemanticObservation"))
        {
            if(s2.equals("virtualsensor"))
            {
                relationship.fieldType = "VirtualSensor";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("VirtualSensor","", "id","vs_id"));
            }
            else if(s2.equals("type"))
            {
                relationship.fieldType = "SemanticObservationType";
                relationship.type = RelationshipType.join;
                relationship.joinInformation.add(new JoinTable("SemanticObservationType","","id","so_type_id"));
            }
            else if(s2.equals("payload"))
            {
                relationship.type = RelationshipType.json;
            }
            else if(!s2.equals("id") && !s2.equals("timestamp"))
            {
                throwUnrecognizedException(s2);
            }
            else
            {
//                if(s2.equals("id"))
//                    relationship.fieldType = "so_id";
                if(s2.equals("timestamp"))
                    relationship.fieldType = "timestamps";  // TODO: check this

                relationship.type = RelationshipType.attribute;
            }
        }
        else
        {
            throwUnrecognizedException(s1);
        }

        return relationship;
    }

    public static String replaceAttributeNames(String initialString)
    {
        String builder = initialString.replace("IP","ip");
        builder = builder.replace("timestamp", "timestamps");
        builder = builder.replace("floor", "floors");
        builder = builder.replace("language", "languages");

        return builder;
    }

    private static void throwUnrecognizedException(String attribute) throws TQLException
    {
        throw new TQLException("Couldn't recognize attribute "+attribute);
    }
}
