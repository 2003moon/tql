package tqllang;

/**
 * Yas
 */
public class MySQLTableMapping
{
    public static String getMySQLNameFor(String collectionName)
    {
        return getMySQLNameForType(CollectionTypeMapping.getTypeOf(collectionName));
    }

    public static String getMySQLNameForType(CollectionType type)
    {
        // TODO:
        switch(type)
        {
            case virtualSensorType:
                return "";
            case virtualSensor:
                return "";
            case user:
                return "";
            case sensorType:
                return "";
            case group:
                return "";
            case infra:
                return "";
            case infraType:
                return "";
            case location:
                return "";
            case observation:
                return "";
            case observationType:
                return "";
            case platform:
                return "";
            case platformType:
                return "";
            case region:
                return "";
            case semanticObservation:
                return "";
            case semanticObsType:
                return "";
            case sensor:
                return "";

            default:
                return "";
        }
    }
}
