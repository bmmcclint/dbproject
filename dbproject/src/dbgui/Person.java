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
public class Person {
  private String queryValue;
  private boolean success;
  private Connection conn;
 
  public boolean create(Connection con, Statement stmt, String[] attributes) throws SQLException {
    success = true;
    queryValue = "insert into person values (" + attributes[0] + "', '" + 
            attributes[1] + "', '" + attributes[2] + "', '" + attributes[3] 
            + "', '" + attributes[4] + "', '" + attributes[5] + "', '" 
            + attributes[6] + "', '" + attributes[7] + ")";
    try {
      System.out.println(queryValue);
      PreparedStatement stmt2 = conn.prepareStatement(queryValue);
      stmt2.executeUpdate();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
      success = false;
    }
    return success;
  }
  
  public void create(Statement stmt, String person_code, String last_name, 
          String first_name, String gender, String email, String addr_code, 
          String phone_num_code) {
    queryValue = "insert into person values (" + person_code + "," + last_name
            + "," + first_name + "," + gender + "," + email + ","
            + addr_code + "," + phone_num_code + ");";
    try {
      stmt.executeUpdate(queryValue);
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
}
