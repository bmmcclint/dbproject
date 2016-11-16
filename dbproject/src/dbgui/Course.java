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
public class Course {
  private String queryValue;
  private boolean success;
  
  public boolean create(Connection conn, Statement stmt, String[] attributes) 
          throws SQLException {
    success = true;
    queryValue = "insert into course values (" + attributes[0] + ", " 
            + attributes[1] + ", " + attributes[2] + ", " + attributes[3] + ", "
            + attributes[4] + ", " + attributes[0] + ");";
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
  
  public void create(Statement stmt, String course_code, String course_title, 
          String course_description, String course_level, String course_status, 
          int cost) throws SQLException {
    queryValue = "insert into course values(" + course_code + ", " 
            + course_title + ", " + course_description + ", " + course_level 
            + ", " + course_status + ", " + cost + ");";
    try {
      stmt.executeUpdate(queryValue);
    }
    catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  public boolean inactive(Statement stmt, String course_code) throws SQLException {
    boolean success = true;
    queryValue = "update course set status = 'inactive' where course_code = " 
            +course_code;
    try {
      stmt.executeUpdate(queryValue);
    }
    catch (SQLException sqle) {
      success = false;
      sqle.printStackTrace();
    }
    return success;
  }
}
