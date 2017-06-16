package tqllang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Yas
 */

public class ReadCSV
{
    public static Connection connect;

    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/tqldb?autoReconnect=true&useSSL=false", "root", "rootpass");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String csvFile = "obs.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String SQL;

        PreparedStatement preparedStatement;

        try
        {

            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null)
            {

                // use comma as separator
                String[] fields = line.split(cvsSplitBy);

                // TODO: make adjustements based on the type we used in MySQL, i.e. whether to keep "
                //fields[0] = "\""+fields[0]+"\"";    // obs id
                fields[1] = fields[1].substring(1,fields[1].length()-1);    // payload
                //fields[1] = "\'"+fields[1]+"\'";
                fields[2] = fields[2].substring(1,fields[2].length()-1);    // timestamp
                //fields[3] = "\""+fields[3]+"\"";        // sensor id
                //fields[4] = "\""+fields[4]+"\"";        // obs type id

                if(sensorExists(fields[3]))
                {
                    SQL = "INSERT INTO Observation (id, sen_id. timestamps, payload, obs_type_id)" +
                            "VALUES (?, ?, ?, ?, ?)";

                    preparedStatement = connect.prepareStatement(SQL);
                    preparedStatement.setString(1, fields[0]);  // obs id
                    preparedStatement.setString(2, fields[3]);  // sen_id
                    preparedStatement.setString(3, fields[2]);  // timestamp
                    preparedStatement.setString(4, fields[1]);  // payload
                    preparedStatement.setString(5, fields[4]);  // obs_type_id

                    preparedStatement.executeUpdate();
                }
                else
                {
                    // insert into Sensor first
                    // TODO: change the parameters
                    SQL = "INSERT INTO Sensor (id, name, description, mobility, loc_id,sen_type_id,pltfm_id,user_id,mac,ip,port)" +
                            "VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?,?)";

                    preparedStatement = connect.prepareStatement(SQL);
                    preparedStatement.setString(1, fields[3]);  // sen id
                    preparedStatement.setString(2,"ss" );  // TODO: hard code anything
                    preparedStatement.setString(3, "ss");  // sen id
                    preparedStatement.setString(4,"adad" );  // TODO: hard code anything
                    preparedStatement.setString(5, "location1");  // sen id
                    preparedStatement.setString(6,"sensor_type1" );  // TODO: hard code anything
                    preparedStatement.setString(7, "platform1");  // sen id
                    preparedStatement.setString(8,"user1" );  // TODO: hard code anything
                    preparedStatement.setString(9,"aa" );  // TODO: hard code anything
                    preparedStatement.setString(10, "ssfa");  // sen id
                    preparedStatement.setString(11,"dfs" );


                    preparedStatement.executeUpdate();

                    // insert into sen_infr
                    // TODO: change the parameters
                    SQL = "INSERT INTO Sen_infr (sen_id,infr_id)" +
                            "VALUES (?, ?)";

                    preparedStatement = connect.prepareStatement(SQL);
                    preparedStatement.setString(1, fields[3]);  // sen id
                    preparedStatement.setString(2,"infra1" );  // TODO: hard code anything

                    preparedStatement.executeUpdate();

                    connect.commit();

                    // insert into Observation
                    SQL = "INSERT INTO Observation (id, sen_id. timestamps, payload, obs_type_id)" +
                            "VALUES (?, ?, ?, ?, ?)";

                    preparedStatement = connect.prepareStatement(SQL);
                    preparedStatement.setString(1, fields[0]);  // obs id
                    preparedStatement.setString(2, fields[3]);  // sen_id
                    preparedStatement.setString(3, fields[2]);  // timestamp
                    preparedStatement.setString(4, fields[1]);  // payload
                    preparedStatement.setString(5, fields[4]);  // obs_type_id

                    preparedStatement.executeUpdate();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean sensorExists(String sensorId)
    {
        try
        {
            String SQL = "SELECT id FROM Sensor WHERE id = (?)";

            PreparedStatement ps = connect.prepareStatement(SQL);
            ps.setString(1,sensorId);
            ResultSet res = ps.executeQuery();

            return res.first();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
