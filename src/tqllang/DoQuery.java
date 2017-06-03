package tqllang;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/5/22.
 */
public class DoQuery {
   ResultSet res;
   public DoQuery(){
        res=null;
   }
   public void Query(String q) {
       try {
           Class.forName("com.mysql.jdbc.Driver").newInstance();
           Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/tqldb?autoReconnect=true&useSSL=false", "root", "rootpass");
           PreparedStatement ps = connect.prepareStatement(q);
           res = ps.executeQuery();
       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
   }
   public void getResult(){
      try{
          if(res!=null){
              JSONObject observation=new JSONObject();
              while(res.next()){
                  observation.put("obs_id",res.getString(1));
                  observation.put("sen_id",res.getString(2));
                  observation.put("timestamp",res.getString(3));
                  observation.put("payload",res.getObject(4));
                  observation.put("type",res.getString(5));
                  System.out.println(observation);
              }

          }
      }catch (Exception e){
          System.out.println(e.getMessage());
      }
   }
}
