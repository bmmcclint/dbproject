/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bmcclint
 */
public class Job_profile {
  private String queryValue;
  private boolean success;
  
  public boolean create(Connection conn, Statement stmt, String attributes[]) 
          throws SQLException {
   success = true;
   queryValue = "insert into job_profile values (" + attributes[0] + "', '" 
           + attributes[1] + "', '" + attributes[2] + "', '" + attributes[3] 
           + "', '"  + attributes[4] + "', '" + attributes[5] + ");";
   try {
     System.out.println(queryValue);
     PreparedStatement stmt2 = conn.prepareStatement(queryValue);
     stmt2.executeUpdate(queryValue);
   }
   catch (SQLException sqle) {
     success = false;
     sqle.printStackTrace();;
   }
   return success;
  }
  
  public void create(Statement stmt, String jp_code, String jp_title, 
          String jp_description, String jp_avg_pay, String req_skill_code) 
          throws SQLException {
    queryValue = "insert into job_profile values (" + jp_code + ", " + jp_title 
            + ", " + jp_description + ", " + jp_avg_pay + ", " + req_skill_code 
            + ");";
    try {
      stmt.executeUpdate(queryValue);
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
}