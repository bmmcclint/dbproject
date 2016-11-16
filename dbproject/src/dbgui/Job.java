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
public class Job {
  private String queryValue;
  private boolean success;
  
  public boolean create(Connection conn, Statement stmt, String[] attributes) throws SQLException {
    queryValue = "insert into job values (" + attributes[0] + ", " 
            + attributes[1] + ", " + attributes[2] + ", " + attributes[3] + ", "
            + attributes[4] + ", " + attributes[5] + ");";
    try {
      System.out.println(queryValue);
      PreparedStatement stmt2 = conn.prepareStatement(queryValue);
      stmt2.executeUpdate();
    }
    catch (SQLException sqle) {
      success = false;
      sqle.printStackTrace();
    }
    return success;
  }
  
  public void create(Statement stmt, String job_code, String job_description, 
          String pay_rate, String pay_type, String comp_code, String jp_code) throws SQLException {
    queryValue = "insert into job values (" + job_code + ", " + job_description
            + ", " + pay_rate + ", " + pay_type + ", " + comp_code + ", "
            + jp_code + ");";
    try {
      stmt.executeUpdate(queryValue);
    }
    catch (SQLException sqle) {
      success = false;
      sqle.printStackTrace();
    }
  }
}
